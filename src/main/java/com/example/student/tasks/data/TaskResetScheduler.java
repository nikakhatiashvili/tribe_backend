package com.example.student.tasks.data;

import com.example.student.tasks.TaskRepository;
import com.example.student.tasks.TribeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskResetScheduler {
    @Autowired
    private TaskRepository taskRepository;

    @Scheduled(cron = "0 0 0 * * *") // runs at midnight
    public void resetTasks() {
        LocalDate currentDate = LocalDate.now();
        //List<TribeTask> tasks = taskRepository.findByDateAndSolved(currentDate, true);
//        for (TribeTask task : tasks) {
//            task.setSolved(false);
//            task.setDateCompleted(null);
//            task.setComment(null);
//            taskRepository.save(task);
//        }
    }
}