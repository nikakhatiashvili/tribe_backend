package com.example.student.user.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.user.JwtTokenUtil;
import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
import com.example.student.user.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository,
                       JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String salt = BCrypt.gensalt();
        String hashedPass = passwordEncoder.encode(tribeUser.getPassword() + salt);
        tribeUser.setPassword(hashedPass);
        tribeUser.setSalt(salt);
        tribeUser.setUserTag(tribeUser.getUsername() + "#" + new Random().nextInt(99999));
        userRepository.save(tribeUser);
    }

    public String signIn(String email, String password) throws UnauthorizedException {

        Optional<TribeUser> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) {throw new UnauthorizedException("Invalid email or password");}

        TribeUser existingUser = user.get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password + existingUser.getSalt(), existingUser.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        return jwtTokenUtil.generateToken(new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>()));
    }
}
