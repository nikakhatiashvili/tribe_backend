package com.example.student.student.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.GlobalExceptionHandler;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
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
    public List<TribeUser> getUsers(){
        return userRepository.findAll();
    }


    public void signUp(TribeUser tribeUser) {
        Optional<TribeUser> userByEmail = userRepository.findUserByFirebaseId(tribeUser.getFirebaseId());
        if (userByEmail.isPresent()){
            try {
                throw new AlreadyExistsException("user is taken");
            } catch (AlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }else {
            userRepository.save(tribeUser);
        }
    }
}
//import static org.junit.Assert.*;
//        import org.junit.Before;
//        import org.junit.Test;
//        import org.mockito.Mock;
//        import org.mockito.MockitoAnnotations;
//
//public class UserServiceTest {
//
//    @Mock
//    private TribeUserRepository tribeUserRepository;
//
//    private UserService userService;
//
//    @Before
//    public void setUp() {
//        // Initialize the mocks
//        MockitoAnnotations.initMocks(this);
//
//        // Create an instance of the UserService class with the mock repository
//        userService = new UserService(tribeUserRepository);
//    }
//
//    @Test
//    public void testSignUp() throws AlreadyExistsException {
//        // Create a new user
//        TribeUser user = new TribeUser("John Doe", "johndoe@example.com", "firebaseId123");
//
//        // Call the signUp method
//        userService.signUp(user);
//
//        // Verify that the repository's save method was called
//        verify(tribeUserRepository, times(1)).save(user);
//    }
//
//    @Test(expected = AlreadyExistsException.class)
//    public void testSignUp_alreadyExists() throws AlreadyExistsException {
//        // Create a new user
//        TribeUser user = new TribeUser("John Doe", "johndoe@example.com", "firebaseId123");
//
//        // Configure the mock repository to throw an AlreadyExistsException when the save method is called
//        doThrow(AlreadyExistsException.class).when(tribeUserRepository).save(user);
//
//        // Call the signUp method
//        userService.signUp(user);
//    }
//}