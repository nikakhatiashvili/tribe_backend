package com.example.student.student.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<TribeUser> getUsers() {
        return userRepository.findAll();
    }

    public void signUp(TribeUser tribeUser) {
        Optional<TribeUser> userByEmail = userRepository.findUserByFirebaseId(tribeUser.getFirebaseId());
        if (userByEmail.isPresent()) {
            try {
                throw new AlreadyExistsException("user is taken");
            } catch (AlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        } else {
            userRepository.save(tribeUser);
        }
    }
}
