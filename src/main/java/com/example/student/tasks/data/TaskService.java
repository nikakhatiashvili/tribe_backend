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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    @Scheduled(cron = "0 0 * * * *")
    public void resetTasks() {
        List<TribeUser> users = userRepository.findAll();

        for (TribeUser user : users) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(user.getTimezone()));
            LocalTime localTime = zonedDateTime.toLocalTime();

            if (localTime.isAfter(LocalTime.parse("23:50")) || localTime.isBefore(LocalTime.parse("00:50"))) {
                List<TribeTask> tasks = getAllTasksForUserInGroups(user);
                System.out.println(tasks);
                for (TribeTask task : tasks) {
                    System.out.println(task.getCompletedTodayBy());
                    task.getCompletedTodayBy().remove(user.getFirebaseId());
                    taskRepository.save(task);
                }
            }
        }
    }

    public void createTask(String firebaseId, TribeTask tribeTask) throws NotFoundException, UnauthorizedException {
        TribeGroup group = getGroupByAdminId(firebaseId);

        if (tribeTask.getEmail() == null) {
            tribeTask.setForAll(true);
        } else if (tribeTask.getEmail() != null) {
            TribeUser taskUser = getUserByEmail(tribeTask.getEmail());
            if (taskUser.getGroups().contains(group.getId())) {
                tribeTask.setEmail(tribeTask.getEmail());
                tribeTask.setForAll(false);
            } else {
                throw new UnauthorizedException("the user is not in your group");
            }
        }

        tribeTask.setGroupId(group.getId());
        taskRepository.save(tribeTask);
    }

    public TasksResponse getTasksForUserInGroup(String firebaseId) throws NotFoundException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        List<TribeGroup> groups = groupRepository.findAllById(user.getGroups());

        List<TribeTask> tasks = getAllTasksForUserInGroups(user);

        for (TribeTask task : tasks) {
            if (task.getCompletedTodayBy().contains(firebaseId)) {
                task.setCompleted(true);
            }
        }

        System.out.println(tasks);
        List<GroupTasksResponse> groupTasks = new ArrayList<>();
        for (TribeGroup group : groups) {
            List<TribeTask> groupTasksList = tasks.stream()
                    .filter(task -> task.getGroupId().equals(group.getId()) && (task.getForAll() || task.getEmail().equals(user.getEmail())))
                    .collect(Collectors.toList());
            GroupTasksResponse groupTasksResponse = new GroupTasksResponse(group.getTribeName(), groupTasksList);
            groupTasks.add(groupTasksResponse);
        }
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

        if (complete) {
            if (!user.getGroups().contains(taskGroup.getId())) {
                throw new UnauthorizedException("User is not authorized to complete this task");
            }
            Set<String> completedTodayBy = task.getCompletedTodayBy();
            if (completedTodayBy.contains(firebaseId)) {
                throw new AlreadyExistsException("Task has already been completed by this user today");
            }
            completedTodayBy.add(firebaseId);
            taskRepository.save(task);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date dates = dateFormat.parse(date);
            System.out.println(dates);
            System.out.println(date);
            String str = String.format("%s Completed Task: %s", user.getName(), task.getName());
            TaskCompletionMessage message = new TaskCompletionMessage(task.getId(), user.getId(), user.getName(), dates, str, taskGroup.getId());
            taskCompletionMessageRepository.save(message);

        } else {
            if (!user.getGroups().contains(taskGroup.getId())) {
                throw new UnauthorizedException("User is not authorized to complete this task");
            }
            Set<String> completedTodayBy = task.getCompletedTodayBy();
            if (!completedTodayBy.contains(firebaseId)) {
                throw new NotFoundException("user hasnt completed task yet so you cant uncompleted it yet");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date dates = dateFormat.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String strDate = formatter.format(dates);
            Optional<TaskCompletionMessage> message = taskCompletionMessageRepository
                    .findByTaskIdAndUserIdAndStrDateAndGroupId(task.getId(), user.getId(), strDate, taskGroup.getId());
            message.ifPresent(taskCompletionMessageRepository::delete);
            completedTodayBy.remove(firebaseId);
            taskRepository.save(task);
        }
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

    public List<TaskCompletionMessage> getMessages(String firebaseId, Long groupId) throws NotFoundException, UnauthorizedException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        if (user.getGroups().contains(groupId)) {
            return taskCompletionMessageRepository.findAllByGroupId(groupId);
        } else {
            throw new UnauthorizedException("you cant see this group-s messages");
        }
    }
}
