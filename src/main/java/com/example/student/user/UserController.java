package com.example.student.user;

import com.example.student.ApiResponse;
import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.user.domain.UserRepository;
import com.example.student.user.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<TribeUser> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user")
    public TribeUser getUser(@RequestParam String firebaseId) throws AlreadyExistsException {
        return userService.getUser(firebaseId);
    }

    @PostMapping("/signup")
    public ApiResponse<String> registerNewUser(@RequestParam String timezone, @RequestParam String username,
                                               @RequestParam String email, @RequestParam String password) {
        try {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = firebaseAuth.createUser(createRequest);
            TribeUser user = new TribeUser();
            user.setFirebaseId(userRecord.getUid());
            user.setEmail(userRecord.getEmail());
            user.setTimezone(timezone);
            user.setHasCreatedGroup(false);
            user.setName(username);
            user.setUsername(username);

            userService.signUp(user);

            return new ApiResponse<>(HttpStatus.OK.value(), "Sign up was successful", null);
        } catch (AlreadyExistsException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage(), null);
        }
    }

    @PostMapping("/signin")
    public ApiResponse<String> signin(@RequestParam String firebaseId) {
        Optional<TribeUser> user = userRepository.findUserByFirebaseId(firebaseId);
        if (!user.isPresent()) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "User does not exist", null);
        } else {
            return new ApiResponse<>(HttpStatus.OK.value(), "Sign in was successful", null);
        }
    }
}
