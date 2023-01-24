package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void createGroup(TribeGroup tribeGroup) throws AlreadyExistsException {
        Optional<TribeUser> admin = userRepository.findUserByFirebaseId(tribeGroup.getAdminId());
        if (admin.isPresent()) {
            System.out.println(admin.get().getGroupId());
            if (admin.get().getGroupId() != null) {
                throw new AlreadyExistsException("user already has a group");
            } else {
                groupRepository.save(tribeGroup);
                admin.get().setGroupId(tribeGroup.getId());
                userRepository.save(admin.get());
            }
        } else {
            throw new IllegalStateException("admin not found");
        }
    }

    public List<TribeUser> getUsersInGroup(String firebaseId) {
        Optional<TribeUser> user = userRepository.findUserByFirebaseId(firebaseId);
        if (user.isPresent()) {
            Long groupId = user.get().getGroupId();
            if (groupId != null) {
                List<TribeUser> memberships = userRepository.findByGroupId(groupId);
                return memberships;
            } else {
                throw new IllegalStateException("user is not in any group");
            }
        } else {
            throw new IllegalStateException("user not found");
        }
    }
}
