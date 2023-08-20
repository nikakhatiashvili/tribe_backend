package com.example.student.tasks.data;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.tasks.domain.CompletedTaskRepository;
import com.example.student.tasks.domain.TaskCompletionMessageRepository;
import com.example.student.tasks.domain.TaskRepository;
import com.example.student.tasks.domain.TasksResponse;
import com.example.student.tasks.model.CompletedTask;
import com.example.student.tasks.model.GroupTasksResponse;
import com.example.student.tasks.model.TaskCompletionMessage;
import com.example.student.tasks.model.TribeTask;
import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final TaskRepository taskRepository;

    private final TaskCompletionMessageRepository taskCompletionMessageRepository;

    private final CompletedTaskRepository completedTaskRepository;

    @Autowired
    public TaskService(GroupRepository groupRepository,
                       TaskRepository taskRepository,
                       UserRepository userRepository,
                       CompletedTaskRepository completedTaskRepository,
                       TaskCompletionMessageRepository taskCompletionMessageRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.taskCompletionMessageRepository = taskCompletionMessageRepository;
        this.completedTaskRepository = completedTaskRepository;
    }

    public void createTask(String firebaseId, TribeTask tribeTask) throws NotFoundException, UnauthorizedException {
        TribeGroup group = getGroupByAdminId(firebaseId);

        if (!tribeTask.getForAll()){
            validateAssignedToUsers(group, tribeTask);
        }
        tribeTask.setGroupId(group.getId());
        taskRepository.save(tribeTask);
    }

    private void validateAssignedToUsers(TribeGroup group, TribeTask tribeTask) throws UnauthorizedException, NotFoundException {
        if (tribeTask.getAssignedTo().isEmpty()){
            throw new NotFoundException("cant assign task to no one");
        }else {
            List<String> assignedTo = tribeTask.getAssignedTo();
            for (String userid: assignedTo){
                TribeUser user = findUserByFirebaseId(userid);
                if (!user.getGroups().contains(group.getId())){
                    throw new UnauthorizedException("One or more assigned users are not in your group");
                }
            }
        }
    }
    public TasksResponse getTasksForUserInGroup(String firebaseId, String date) throws NotFoundException, ParseException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        List<TribeTask> tasks = getAllTasksForUserInGroups(user);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date dates = dateFormat.parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(dates);
        List<TaskCompletionMessage> completedTasks = taskCompletionMessageRepository.findByUserIdAndStrDate(user.getId(), strDate);
        for (TribeTask task : tasks) {
            task.setCompleted(completedTasks.stream().anyMatch(t -> t.getTaskId() == task.getId()));
        }
        List<GroupTasksResponse> groupTasks = groupRepository.findAllById(user.getGroups()).stream()
                .map(group -> {
                    List<TribeTask> groupTasksList = tasks.stream()
                            .filter(task -> task.getGroupId().equals(group.getId()) && (task.getForAll()) || task.getAssignedTo().contains(user.getFirebaseId()))
                            .collect(Collectors.toList());
                    return new GroupTasksResponse(group.getTribeName(), groupTasksList);
                })
                .collect(Collectors.toList());
        return new TasksResponse(groupTasks);
    }

    public List<TribeTask> getAllTasksForUserInGroups(TribeUser user) {
        List<TribeTask> tasks = new ArrayList<>();
        List<TribeGroup> groups = groupRepository.findAllById(user.getGroups());
        for (TribeGroup group : groups) {
            tasks.addAll(taskRepository.findByGroupId(group.getId()));
        }
        return tasks;
    }

    public void updateTask(String firebaseId, long taskId, boolean complete, String date) throws NotFoundException, UnauthorizedException, AlreadyExistsException, ParseException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        TribeTask task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        TribeGroup taskGroup = groupRepository.findById(task.getGroupId()).orElseThrow(() -> new NotFoundException("Group not found"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date dates = dateFormat.parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(dates);

        if (!user.getGroups().contains(taskGroup.getId())) {
            throw new UnauthorizedException("User is not authorized to complete this task");
        }

        Optional<TaskCompletionMessage> isCompleted = taskCompletionMessageRepository.findByUserIdAndStrDateAndTaskId(user.getId(), strDate, taskId);
        Set<String> completedTodayBy = task.getCompletedTodayBy();


        if (complete) {
            if (isCompleted.isPresent()) {
                throw new AlreadyExistsException("Task has already been completed by this user today");
            }
            completedTodayBy.add(firebaseId);
            String str = String.format("%s Completed Task: %s", user.getName(), task.getName());
            TaskCompletionMessage message = new TaskCompletionMessage(task.getId(), user.getId(), user.getName(), dates, str, taskGroup.getId());
            taskCompletionMessageRepository.save(message);
        } else {
            if (isCompleted.isEmpty()) {
                throw new NotFoundException("User hasn't completed task yet, so you can't uncomplete it yet");
            }
            Optional<TaskCompletionMessage> message = taskCompletionMessageRepository.findByTaskIdAndUserIdAndStrDateAndGroupId(task.getId(), user.getId(), strDate, taskGroup.getId());
            message.ifPresent(taskCompletionMessageRepository::delete);
            completedTodayBy.remove(firebaseId);
        }
        taskRepository.save(task);
    }

    public List<CompletedTask> getCompletedTasks(String firebaseId) {
        return completedTaskRepository.findAllByUserId(firebaseId);
    }

    private TribeGroup getGroupByAdminId(String adminFirebaseId) throws NotFoundException {
        return groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new NotFoundException("Firebase ID does not match admin ID"));
    }

    private TribeUser findUserByFirebaseId(String firebaseId) throws NotFoundException {
        return userRepository.findUserByFirebaseId(firebaseId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TribeUser getUserByEmail(String userEmail) throws NotFoundException {
        return userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
    }

    public Page<TaskCompletionMessage> getMessages(String firebaseId, Long groupId, int pageNumber) throws NotFoundException, UnauthorizedException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        if (user.getGroups().contains(groupId)) {
            Pageable pageable = PageRequest.of(pageNumber -1, 20, Sort.by("date").descending());
            return taskCompletionMessageRepository.findAllByGroupId(groupId, pageable);
        } else {
            throw new UnauthorizedException("you cant see this group-s messages");
        }
    }
}
