package com.example.student.tasks.model;

public class TaskAssignment {
    private Long taskId;
    private Long userId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TaskAssignment(Long taskId, Long userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

    // Getters and setters
}