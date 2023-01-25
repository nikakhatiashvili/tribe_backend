package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void createGroup(TribeGroup group) throws AlreadyExistsException {
        TribeUser admin = userRepository.findUserByFirebaseId(group.getAdminId())
                .orElseThrow(() -> new IllegalStateException("Admin not found"));
        if (admin.getGroupId() != null) {
            throw new AlreadyExistsException("Admin already has a group");
        }
        groupRepository.save(group);
        admin.setGroupId(group.getId());
        userRepository.save(admin);
    }

    public List<TribeUser> getUsersInGroup(String firebaseId) throws Exception {
        TribeUser user = userRepository.findUserByFirebaseId(firebaseId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (user.getGroupId() == null) {
            throw new IllegalStateException("User is not in any group");
        }
        return userRepository.findByGroupId(user.getGroupId());
    }
}
