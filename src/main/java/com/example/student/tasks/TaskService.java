package com.example.student.tasks;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import com.example.student.tasks.model.*;
import com.example.student.tasks.model.TasksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public void createTask(String firebaseId, TribeTask tribeTask) throws NotFoundException {
        TribeGroup group = getGroupByAdminId(firebaseId);
        List<TribeUser> members = userRepository.findByGroupsContaining(group.getId());
        List<String> assignedTo = new ArrayList<>();
        for (TribeUser member : members) {
            assignedTo.add(member.getFirebaseId());
        }
        tribeTask.setAssignedTo(assignedTo);
        tribeTask.setGroupId(group.getId());
        taskRepository.save(tribeTask);
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

    public TasksResponse getTasksForUserInGroup(String firebaseId) throws NotFoundException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        List<TribeGroup> groups = groupRepository.findAllById(user.getGroups());

        List<TribeTask> tasks = new ArrayList<>();

        for (TribeGroup group : groups) {
            tasks.addAll(taskRepository.findByGroupIdAndAssignedToContaining(group.getId(), firebaseId));
        }

        // Check if it's past 12 pm in each user's local time and remove them from completedTodayBy if it is
        String timezone = user.getTimezone();
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(timezone));

        for (TribeTask task : tasks) {
            Map<String, LocalDateTime> completedTodayBy = task.getCompletedTodayBy();
            if (completedTodayBy.containsKey(firebaseId)) {
                LocalDateTime completedTime = completedTodayBy.get(firebaseId);

                if (currentTime.getDayOfYear() > completedTime.getDayOfYear()) {
                    completedTodayBy.remove(firebaseId);
                    taskRepository.save(task); // Update the task in the database
                }
            }
        }

        List<GroupTasksResponse> groupTasks = new ArrayList<>();
        for (TribeGroup group : groups) {
            List<TribeTask> groupTasksList = tasks.stream()
                    .filter(task -> task.getGroupId().equals(group.getId()))
                    .collect(Collectors.toList());
            GroupTasksResponse groupTasksResponse = new GroupTasksResponse(group.getTribeName(), groupTasksList);
            groupTasks.add(groupTasksResponse);
        }

        return new TasksResponse(groupTasks);
    }

    public void completeTask(String firebaseId, long taskId,String comment) throws NotFoundException, UnauthorizedException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        TribeTask task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));


        // Check if the task belongs to the user's group
        TribeGroup taskGroup = groupRepository.findById(task.getGroupId()).orElseThrow(() -> new NotFoundException("Group not found"));
        if (!user.getGroups().contains(taskGroup.getId())) {
            throw new UnauthorizedException("User is not authorized to complete this task");
        }

        // Check if the user has already completed the task today
        String firebaseIdStr = String.valueOf(firebaseId);
        Map<String, LocalDateTime> completedTodayBy = task.getCompletedTodayBy();
        if (completedTodayBy.containsKey(firebaseIdStr)) {
            throw new IllegalArgumentException("Task has already been completed by this user today");
        }

        // Add the user's firebaseId to the completedTodayBy map
        LocalDateTime completedTime = LocalDateTime.now();
        completedTodayBy.put(firebaseIdStr, completedTime);

        // Save the updated task in the database
        taskRepository.save(task);

        // Create a new CompletedTask object and save it to the database
        CompletedTask completedTask = new CompletedTask(firebaseIdStr, taskId, completedTime, comment);
        completedTaskRepository.save(completedTask);
    }

    public List<CompletedTask> getCompletedTasks(String firebaseId) {
        return completedTaskRepository.findAll();
    }
}
//    public void removeHabit(String firebaseId, Long id, String email) throws Exception {
//        TribeGroup group = groupRepository.getGroupByAdminId(firebaseId)
//                .orElseThrow(() -> new UnauthorizedException("user with this firebase id is not admin of any group"));
//        TribeHabit habit = habitRepository.findByGroupIdAndId(group.getId(), id)
//                .orElseThrow(() -> new NotFoundException("habit not found with id " + id));
//        habitRepository.delete(habit);
//    }
//


