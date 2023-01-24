package com.example.student.student.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;
import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository tribeUserRepository;

    private UserService userService;

    @Before
    public void setUp() {
        // Initialize the mocks
        MockitoAnnotations.initMocks(this);

        // Create an instance of the UserService class with the mock repository
        userService = new UserService(tribeUserRepository);
    }

    @Test
    public void testSignUp() throws AlreadyExistsException {
        // Create a new user
        TribeUser user = new TribeUser("John Doe", "johndoe@example.com", "firebaseId123");

        // Call the signUp method
        userService.signUp(user);

        // Verify that the repository's save method was called
        verify(tribeUserRepository, times(1)).save(user);
    }

    @Test
    public void testSignUp_alreadyExists()  {
        // Create a new user
        TribeUser user = new TribeUser("John Doe", "johndoe@example.com", "wasdafddahaasdsadasgsasasaashsgsahgsasasdddasd");

        // Configure the mock repository to throw an AlreadyExistsException when the save method is called
        doThrow(AlreadyExistsException.class).when(tribeUserRepository).save(user);

        // Call the signUp method
        userService.signUp(user);
    }
}