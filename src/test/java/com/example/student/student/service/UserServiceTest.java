package com.example.student.student.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void signUp_whenUserDoesNotExist_shouldSaveUser() throws Exception {
        TribeUser user = new TribeUser();
        user.setFirebaseId("test-firebase-id");
        user.setEmail("test-email@example.com");

        when(userRepository.findUserByFirebaseId(user.getFirebaseId())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        userService.signUp(user);

        verify(userRepository).save(user);
    }

    @Test(expected = AlreadyExistsException.class)
    public void signUp_whenUserExistsWithEmail_shouldThrowAlreadyExistsException() throws Exception {
        TribeUser user = new TribeUser();
        user.setFirebaseId("test-firebase-id");
        user.setEmail("test-email@example.com");
        user.setName("test-nika");

        when(userRepository.findUserByFirebaseId(user.getFirebaseId())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(new TribeUser()));
        System.out.println("test");

        userService.signUp(user);


    }

    @Test(expected = AlreadyExistsException.class)
    public void signUp_whenUserExistsWithFirebaseId_shouldThrowAlreadyExistsException() throws Exception {
        // Arrange
        TribeUser user = new TribeUser();
        user.setFirebaseId("test-firebase-id");
        user.setEmail("test-email@example.com");
        user.setName("test-nika");

        when(userRepository.findUserByFirebaseId(user.getFirebaseId())).thenReturn(Optional.of(new TribeUser()));
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        // Act
        userService.signUp(user);
        System.out.println("test");
        // Assert
        // The test is expected to throw an AlreadyExistsException
    }
}
