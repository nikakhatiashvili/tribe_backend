package com.example.student.habit;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabitService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final HabitRepository habitRepository;

    @Autowired
    public HabitService(GroupRepository groupRepository,
                        HabitRepository habitRepository,
                        UserRepository userRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.habitRepository = habitRepository;
    }

    public void createNewHabit(TribeHabit tribeHabit, String firebaseId) throws Exception {
        TribeGroup group = groupRepository.getGroupByAdminId(firebaseId)
                .orElseThrow(() -> new UnauthorizedException("user with this firebase id is not admin of any group"));
        tribeHabit.setGroupId(group.getId());
        habitRepository.save(tribeHabit);
    }

//    public List<TribeHabit> getHabits(String firebaseId) throws Exception {
//        TribeUser user = userRepository.findUserByFirebaseId(firebaseId)
//                .orElseThrow(() -> new NotFoundException("user not found with that id" + firebaseId));
//        if (user.getGroupId() == null) {
//            return List.of();
//        }
//        return habitRepository.findByGroupId(user.getGroupId());
//    }

//    public void removeHabit(String firebaseId, Long id, String email) throws Exception {
//        TribeGroup group = groupRepository.getGroupByAdminId(firebaseId)
//                .orElseThrow(() -> new UnauthorizedException("user with this firebase id is not admin of any group"));
//        TribeHabit habit = habitRepository.findByGroupIdAndId(group.getId(), id)
//                .orElseThrow(() -> new NotFoundException("habit not found with id " + id));
//        habitRepository.delete(habit);
//    }

//    public void completeHabit(String firebaseId, Long habitId) throws NotFoundException, UnauthorizedException {
//        TribeUser user = userRepository.findUserByFirebaseId(firebaseId)
//                .orElseThrow(() -> new UnauthorizedException("user with this firebase id is not admin of any group"));
//
//        TribeHabit habit = habitRepository.findById(habitId).orElseThrow(() -> new NotFoundException("habit not found with id "));
//        TribeGroup group = groupRepository.findById(user.getGroupId()).orElseThrow(() -> new NotFoundException("not found"));
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime completionTime = habit.getCompletionTime();
//
//        if (completionTime == null || now.isAfter(completionTime.plusHours(24))) {
//            habit.setCompletionTime(now);
//            habitRepository.save(habit);
//        }
//    }

}
