package com.example.student.tasks;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.tasks.model.CompletedTask;
import com.example.student.tasks.model.TasksResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void createNewTask(@Valid @RequestBody TribeTask tribeTask, @RequestParam String firebaseId) throws Exception {
        taskService.createTask(firebaseId,tribeTask);
    }

    @GetMapping("/get_tasks")
    public TasksResponse getTasks(@RequestParam String firebaseId) throws Exception {
        return taskService.getTasksForUserInGroup(firebaseId);
    }

    @RequestMapping(value = "/complete_task", method = RequestMethod.PUT)
    public ResponseEntity<String> completeTask(@RequestParam String firebaseId, @RequestParam long taskId, @RequestParam String comment) throws UnauthorizedException {
        try {
            taskService.completeTask(firebaseId, taskId,comment);
            return ResponseEntity.ok("Task completed successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/completed_tasks")
    public List<CompletedTask> getCompletedTasks(@RequestParam String firebaseId) {
        return taskService.getCompletedTasks(firebaseId);
    }

//    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        taskService.removeTask(firebaseId, id);
//    }

}


