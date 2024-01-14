package com.example.student.tasks;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.tasks.data.TaskService;
import com.example.student.tasks.domain.TasksResponse;
import com.example.student.tasks.model.CompletedTask;
import com.example.student.tasks.model.TaskCompletionMessage;
import com.example.student.tasks.model.TribeTask;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.student.ApiResponse;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ApiResponse<String> createNewTask(@Valid @RequestBody TribeTask tribeTask, @RequestParam String firebaseId) {
        try {
            taskService.createTask(firebaseId, tribeTask);

            return new ApiResponse<>(HttpStatus.OK.value(), "Task added", null);
        } catch (UnauthorizedException e) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/get_tasks")
    public ApiResponse<TasksResponse> getTasks(@RequestParam String firebaseId, @RequestParam String date) throws Exception {
        TasksResponse tasksResponse = taskService.getTasksForUserInGroup(firebaseId, date);
        return new ApiResponse<>(HttpStatus.OK.value(), "Tasks retrieved successfully", tasksResponse);
    }

    @PostMapping("/complete_task")
    public ApiResponse<String> updateTask(
            @RequestParam String firebaseId, @RequestParam long taskId, @RequestParam boolean complete, @RequestParam String date) {
        try {
            taskService.updateTask(firebaseId, taskId, complete, date);
            return new ApiResponse<>(HttpStatus.OK.value(), "Task updated successfully", null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (AlreadyExistsException e) {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        } catch (UnauthorizedException e) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/messages")
    public ApiResponse<Page<TaskCompletionMessage>> getMessages(@RequestParam String firebaseId, @RequestParam Long groupId, @RequestParam int pageNumber) {
        try {
            Page<TaskCompletionMessage> messages = taskService.getMessages(firebaseId, groupId, pageNumber);
            return new ApiResponse<>(HttpStatus.OK.value(), "Messages retrieved successfully", messages);
        } catch (NotFoundException | UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/completed_tasks")
    public ApiResponse<List<CompletedTask>> getCompletedTasks(@RequestParam String firebaseId) {
        List<CompletedTask> completedTasks = taskService.getCompletedTasks(firebaseId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Completed tasks retrieved successfully", completedTasks);
    }

    //    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        taskService.removeTask(firebaseId, id);
//    }
}
