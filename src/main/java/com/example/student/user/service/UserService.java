package com.example.student.user.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.service.GroupRepository;
import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
import com.example.student.user.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
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

    public void signUp(TribeUser tribeUser) throws AlreadyExistsException, UnauthorizedException {

        boolean userExists = userRepository.getUserByEmail(tribeUser.getEmail()).isPresent();

        boolean isPasswordValid = PasswordValidator.isValidPassword(tribeUser.getPassword());

        if (userExists) {
            throw new UnauthorizedException("user is taken");
        }
        if (!isPasswordValid)
            throw new UnauthorizedException("invalid password");

        tribeUser.setUserTag(tribeUser.getUsername() + "#" + new Random().nextInt(99999));
        userRepository.save(tribeUser);
    }

//
//    public static boolean isValidPassword(String password) {
//        PasswordValidator pass = new PasswordValidator();
//        return pass
//        // Check length
//        if (password.length() < 7) {
//            return false;
//        }
//        boolean hasUppercase = false;
//        boolean hasLowercase = false;
//        boolean hasSpecialChar = false;
//
//        for (char c : password.toCharArray()) {
//            if (Character.isUpperCase(c)) {
//                hasUppercase = true;
//            } else if (Character.isLowerCase(c)) {
//                hasLowercase = true;
//            } else if (Character.isDigit(c) || !Character.isLetterOrDigit(c)) {
//                hasSpecialChar = true;
//            }
//
//            if (hasUppercase && hasLowercase && hasSpecialChar) {
//                break;
//            }
//        }
//
//        return hasUppercase && hasLowercase && hasSpecialChar;
//    }
}
