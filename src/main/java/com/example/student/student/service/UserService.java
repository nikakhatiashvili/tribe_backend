package com.example.student.student.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public List<TribeUser> getUsers() {
        return userRepository.findAll();
    }

    public void signUp(TribeUser tribeUser) throws Exception {
        boolean userExists = userRepository.findUserByFirebaseId(tribeUser.getFirebaseId()).isPresent()
                || userRepository.getUserByEmail(tribeUser.getEmail()).isPresent();
        if (userExists) {
            throw new AlreadyExistsException("user is taken");
        }
        userRepository.save(tribeUser);
    }

    public void addUserToGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeUser admin = userRepository.findUserByFirebaseId(adminFirebaseId)
                .orElseThrow(() -> new NotFoundException("user with that firebaseid is not admin of any group"));

        TribeGroup group = groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new UnauthorizedException("Firebase ID does not match admin ID"));

        TribeUser userToAdd = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
        if (userToAdd.getGroupId() != null) {
            throw new AlreadyExistsException("User already belongs to a group");
        }

        userToAdd.setGroupId(admin.getGroupId());
        userRepository.save(userToAdd);
    }

    public void removeUserFromGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeUser admin = userRepository.findUserByFirebaseId(adminFirebaseId)
                .orElseThrow(() -> new NotFoundException("Admin user not found"));
        if (admin.getGroupId() == null) {
            throw new NotFoundException("Admin user is not in a group");
        }

        TribeGroup group = groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new UnauthorizedException("Firebase ID does not match admin ID"));

        TribeUser userToRemove = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
        if (userToRemove.getGroupId() == null) {
            throw new NotFoundException("User is not in any group");
        }
        if (userToRemove.getGroupId() != admin.getGroupId()) {
            throw new UnauthorizedException("This user is not in your group");
        }
        userToRemove.setGroupId(null);
        userRepository.save(userToRemove);
    }

}
