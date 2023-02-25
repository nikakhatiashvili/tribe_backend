package com.example.student.tasks;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/habit")
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
    public Map<String, List<TribeTask>> getTasks(@RequestParam String firebaseId) throws Exception {
        return taskService.getTasksForUserInGroup(firebaseId);
    }
//
//    @DeleteMapping("/remove")
//    public void removeHabit(@RequestParam String firebaseId, @RequestParam Long id) throws Exception {
//        taskService.removeTask(firebaseId, id);
//    }

}


