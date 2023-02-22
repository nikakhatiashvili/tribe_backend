package com.example.student.student.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.service.GroupRepository;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void signUp_whenUserDoesNotExist_shouldSaveUser() throws Exception {
        // Arrange
        TribeUser user = new TribeUser();
        user.setFirebaseId("test-firebase-id");
        user.setEmail("test-email@example.com");

        when(userRepository.findUserByFirebaseId(user.getFirebaseId())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        userService.signUp(user);

        // Assert
        verify(userRepository).save(user);
    }

    @Test(expected = AlreadyExistsException.class)
    public void signUp_whenUserExistsWithEmail_shouldThrowAlreadyExistsException() throws Exception {
        // Arrange
        TribeUser user = new TribeUser();
        user.setFirebaseId("test-firebase-id");
        user.setEmail("test-email@example.com");
        user.setName("test-nika");

        when(userRepository.findUserByFirebaseId(user.getFirebaseId())).thenReturn(Optional.empty());
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(new TribeUser()));
        System.out.println("test");
        // Act
        userService.signUp(user);

        // Assert
        // The test is expected to throw an AlreadyExistsException
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
