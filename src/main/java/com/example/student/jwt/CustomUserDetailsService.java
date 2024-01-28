package com.example.student.jwt;

import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TribeUser tribeUser = userRepository.findUserByFirebaseId(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(tribeUser.getUsername(), tribeUser.getEmail(), new ArrayList<>());
    }
}