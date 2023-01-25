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
import java.util.Optional;

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

    public void signUp(TribeUser tribeUser) {
        Optional<TribeUser> userByFirebaseId = userRepository.findUserByFirebaseId(tribeUser.getFirebaseId());
        Optional<TribeUser> userByEmail = userRepository.getUserByEmail(tribeUser.getEmail());
        if (userByFirebaseId.isPresent() || userByEmail.isPresent()) {
            throw new AlreadyExistsException("user is taken");
        } else {
            userRepository.save(tribeUser);
        }
    }

    public void addUserToGroup(String firebaseId, String email) {
        Optional<TribeUser> user = userRepository.findUserByFirebaseId(firebaseId);
        if (user.isPresent() && user.get().getGroupId() != null) {
            Optional<TribeGroup> group = groupRepository.getGroupByAdminId(firebaseId);
            if (group.isPresent() && user.get().getFirebaseId().equals(group.get().getAdminId())) {
                Optional<TribeUser> userToAdd = userRepository.getUserByEmail(email);
                if (userToAdd.isPresent()) {
                    if (userToAdd.get().getGroupId() == null) {
                        userToAdd.get().setGroupId(user.get().getGroupId());
                        userRepository.save(userToAdd.get());
                    } else {
                        throw new AlreadyExistsException("User already belongs to a group");
                    }
                } else {
                    throw new NotFoundException("User not found with email: " + email);
                }
            } else {
                throw new UnauthorizedException("Firebase ID does not match admin ID");
            }
        } else {
            throw new NotFoundException("user not found or not in a group");
        }
    }
}
