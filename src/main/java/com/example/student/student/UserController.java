package com.example.student.student;

import com.example.student.student.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void registerNewStudent(@Valid @RequestBody TribeUser tribeUser) {
        userService.signUp(tribeUser);
    }

    @PostMapping("/adduser")
    public void addUserToGroup(@RequestParam String firebaseId, @RequestParam String email) {
        userService.addUserToGroup(firebaseId, email);
    }
}
