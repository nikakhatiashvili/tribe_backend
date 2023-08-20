package com.example.student.user.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.service.GroupRepository;
import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    @GetMapping
    public TribeUser getUser(String firebaseId) throws AlreadyExistsException {

        Optional<TribeUser> user = userRepository.findUserByFirebaseId(firebaseId);

        if (!user.isPresent()) {
            throw new AlreadyExistsException("user does not exists");
        }else {
            return user.get();
        }
    }

    public void signUp(TribeUser tribeUser) throws AlreadyExistsException {
        boolean userExists = userRepository.findUserByFirebaseId(tribeUser.getFirebaseId()).isPresent()
                || userRepository.getUserByEmail(tribeUser.getEmail()).isPresent();
        if (userExists) {
            throw new AlreadyExistsException("user is taken");
        }

        tribeUser.setUserTag(tribeUser.getUsername() + "#" + new Random().nextInt(99999));
        userRepository.save(tribeUser);
    }

}
