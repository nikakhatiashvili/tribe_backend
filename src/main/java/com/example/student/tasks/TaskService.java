package com.example.student.tasks;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(GroupRepository groupRepository,
                       TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
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

    public Map<String, List<TribeTask>> getTasksForUserInGroup(String firebaseId) throws NotFoundException {
        TribeUser user = findUserByFirebaseId(firebaseId);
        List<TribeGroup> groups = groupRepository.findAllById(user.getGroups());

        Map<String, List<TribeTask>> tasksByGroup = new HashMap<>();

        for (TribeGroup group : groups) {
            List<TribeTask> tasks = taskRepository.findByGroupIdAndAssignedToContaining(group.getId(), firebaseId);
            tasksByGroup.put(group.getTribeName(), tasks);
        }
        return tasksByGroup;
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


