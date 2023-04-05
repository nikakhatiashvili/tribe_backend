package com.example.student.tasks;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

//    @Test
//    public void testResetTasks() {
//        // Set up test data
//        TribeUser user1 = new TribeUser("gabi", "user1@example.com", "123askdjasd723jkhasd8ase12");
//        user1.setTimezone("Asia/Tokyo");
//        user1.getGroups().add(1L);
//        TribeUser user2 = new TribeUser("nika", "user2@example.com", "9ahkjashdkjahsdkfjhasdkjfhasdkjfhasdkjfhasdkjfhaskdjf");
//        user1.setTimezone("Asia/Tokyo");
//        user1.getGroups().add(1L);
//
//        List<TribeUser> users = Arrays.asList(user1, user2);
//
//        TribeTask task1 = new TribeTask("Task 1", "Description 1", 1L);
//        task1.getCompletedTodayBy().add(user1.getFirebaseId());
//        task1.getCompletedTodayBy().add(user2.getFirebaseId());
//
//        TribeTask task2 = new TribeTask("Task 2", "Description 2", 1L);
//        task1.getCompletedTodayBy().add(user1.getFirebaseId());
//        task1.getCompletedTodayBy().add(user2.getFirebaseId());
//
//        List<TribeTask> tasks = Arrays.asList(task1, task2);
//
//        // Set up mock repository behavior
//        when(userRepository.findAll()).thenReturn(users);
//        when(taskService.getAllTasksForUserInGroups(user1)).thenReturn(tasks);
//        when(taskService.getAllTasksForUserInGroups(user2)).thenReturn(tasks);
//
//        Instant now = Instant.now();
//        ZoneId timezone = ZoneId.of(user1.getTimezone());
//        ZonedDateTime userTime = now.atZone(ZoneOffset.UTC).withZoneSameInstant(timezone);
//
//        // Call the method being tested
//        taskService.resetTasks();
//
//        // Verify that the tasks were reset for each user whose timezone is currently midnight
//        verify(taskRepository).save(task1);
//        verify(taskRepository).save(task2);
//        assertTrue(task1.getCompletedTodayBy().isEmpty());
//        assertTrue(task2.getCompletedTodayBy().isEmpty());
//    }
}