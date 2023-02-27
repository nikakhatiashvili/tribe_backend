package com.example.student.tasks;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import com.example.student.tasks.model.CompletedTask;
import com.example.student.tasks.model.GroupTasksResponse;
import com.example.student.tasks.model.TasksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;

    private final CompletedTaskRepository completedTaskRepository;

    @Autowired
    public TaskService(GroupRepository groupRepository,
                       TaskRepository taskRepository,
                       UserRepository userRepository,
                       CompletedTaskRepository completedTaskRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.completedTaskRepository = completedTaskRepository;
    }

//    @Scheduled(fixedRate = 60 * 60 * 1000) // run every hour
//    public void resetTasks() {
//
//    }

    //    @Scheduled(fixedRate = 60000) // Run every minute
//    public void resetTasksForAllUsers() {
//        List<TribeUser> users = userRepository.findAll();
//        for (TribeUser user : users) {
//            ResetTasksForUsers(user);
//        }
//    }
    public void createTask(String firebaseId, TribeTask tribeTask) throws NotFoundException, UnauthorizedException {
        TribeGroup group = getGroupByAdminId(firebaseId);

        if (tribeTask.getEmail() == null) {
            tribeTask.setForAll(true);
        } else if (tribeTask.getEmail() != null) {
            TribeUser taskUser = getUserByEmail(tribeTask.getEmail());
            if (taskUser.getGroups().contains(group.getId())){
                tribeTask.setEmail(tribeTask.getEmail());
                tribeTask.setForAll(false);
            }else {
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

    public void completeTask(String firebaseId, long taskId, String comment) throws NotFoundException, UnauthorizedException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        TribeTask task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));

        // Check if the task belongs to the user's group
        TribeGroup taskGroup = groupRepository.findById(task.getGroupId()).orElseThrow(() -> new NotFoundException("Group not found"));
        if (!user.getGroups().contains(taskGroup.getId())) {
            throw new UnauthorizedException("User is not authorized to complete this task");
        }

        // Check if the user has already completed the task today
        String firebaseIdStr = String.valueOf(firebaseId);
        Set<String> completedTodayBy = task.getCompletedTodayBy();
        if (completedTodayBy.contains(firebaseIdStr)) {
            throw new IllegalArgumentException("Task has already been completed by this user today");
        }

        // Add the user's firebaseId to the completedTodayBy map
        LocalDateTime completedTime = LocalDateTime.now();
        completedTodayBy.add(firebaseIdStr);

        // Save the updated task in the database
        taskRepository.save(task);

        // Create a new CompletedTask object and save it to the database
        CompletedTask completedTask = new CompletedTask(firebaseIdStr, taskId, completedTime, comment);
        completedTaskRepository.save(completedTask);
    }

    public List<CompletedTask> getCompletedTasks(String firebaseId) {
        return completedTaskRepository.findAll();
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
}

