package com.example.student.habit;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<TribeHabit> getHabits(String firebaseId) throws Exception {
        TribeUser user = userRepository.findUserByFirebaseId(firebaseId)
                .orElseThrow(() -> new NotFoundException("user not found with that id" + firebaseId));
        if (user.getGroupId() == null) {
            return List.of();
        }
        return habitRepository.findByGroupId(user.getGroupId());
    }
}
