package com.example.student.user;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<TribeUser> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerNewStudent(@Valid @RequestBody TribeUser tribeUser) {
        try {
            userService.signUp(tribeUser);
            return ResponseEntity.ok("Sign up was successful");
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }
}
