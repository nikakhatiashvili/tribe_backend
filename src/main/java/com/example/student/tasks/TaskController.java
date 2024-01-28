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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<String>> createNewTask(@Valid @RequestBody TribeTask tribeTask, @RequestParam String firebaseId) {
        try {
            taskService.createTask(firebaseId, tribeTask);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Task successfully added", null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/get_tasks")
    public ResponseEntity<ApiResponse<TasksResponse>> getTasks(@RequestParam String firebaseId, @RequestParam String date) {
        try {
            TasksResponse tasksResponse = taskService.getTasksForUserInGroup(firebaseId, date);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Tasks retrieved successfully", tasksResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/complete_task")
    public ResponseEntity<ApiResponse<String>> updateTask(
            @RequestParam String firebaseId, @RequestParam long taskId, @RequestParam boolean complete, @RequestParam String date) {
        try {
            taskService.updateTask(firebaseId, taskId, complete, date);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Task updated successfully", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null));
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error parsing date", null));
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<Page<TaskCompletionMessage>>> getMessages(@RequestParam String firebaseId, @RequestParam Long groupId, @RequestParam int pageNumber) {
        try {
            Page<TaskCompletionMessage> messages = taskService.getMessages(firebaseId, groupId, pageNumber);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Messages retrieved successfully", messages));
        } catch (NotFoundException | UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/completed_tasks")
    public ResponseEntity<ApiResponse<List<CompletedTask>>> getCompletedTasks(@RequestParam String firebaseId) {
        List<CompletedTask> completedTasks = taskService.getCompletedTasks(firebaseId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Completed tasks retrieved successfully", completedTasks));
    }

// Uncomment and refactor this method if needed
// @DeleteMapping("/remove")
// public ResponseEntity<ApiResponse<String>> removeHabit(@RequestParam String firebaseId, @RequestParam Long id) {
//     try {
//         taskService.removeTask(firebaseId, id);
//         return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Task removed successfully", null));
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
//     }
// }
}
