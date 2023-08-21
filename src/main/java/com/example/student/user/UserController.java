package com.example.student.user;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.user.domain.UserRepository;
import com.example.student.user.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public UserController(UserService userService,UserRepository userRepository) {
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
    public ResponseEntity<String> registerNewUser(@RequestParam String timezone,@RequestParam String username,@RequestParam String email, @RequestParam String password) {
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

            userService.signUp(user);

            return ResponseEntity.ok("Sign up was successful");
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (FirebaseAuthException | IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestParam String firebaseId){
        Optional<TribeUser> user = userRepository.findUserByFirebaseId(firebaseId);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exists");
        }else {
            return ResponseEntity.ok("Sign in was successful");
        }
    }

}
