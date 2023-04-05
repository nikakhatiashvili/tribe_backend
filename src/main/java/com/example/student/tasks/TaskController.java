package com.example.student.tasks;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.CustomErrorResponse;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.tasks.model.CompletedTask;
import com.example.student.tasks.model.TasksResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewTask(@Valid @RequestBody TribeTask tribeTask, @RequestParam String firebaseId) {
        try {
            taskService.createTask(firebaseId, tribeTask);

            // Create a Map to hold the JSON response
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Task added");

            return ResponseEntity.ok(responseMap);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }

    }

    @GetMapping("/get_tasks")
    public TasksResponse getTasks(@RequestParam String firebaseId) throws Exception {
        return taskService.getTasksForUserInGroup(firebaseId);
    }

    @PostMapping("/complete_task")
    public ResponseEntity<Object> updateTask(
            @RequestParam String firebaseId, @RequestParam long taskId, @RequestParam boolean complete) {
        try {
            taskService.updateTask(firebaseId, taskId, complete);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Task updated successfully");

            return ResponseEntity.ok(responseMap);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    //    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        taskService.removeTask(firebaseId, id);
//    }

    @GetMapping("/completed_tasks")
    public List<CompletedTask> getCompletedTasks(@RequestParam String firebaseId) {
        return taskService.getCompletedTasks(firebaseId);
    }

//    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        taskService.removeTask(firebaseId, id);
//    }

}
