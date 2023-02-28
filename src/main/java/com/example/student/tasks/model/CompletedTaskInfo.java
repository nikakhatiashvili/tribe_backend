package com.example.student.tasks.model;

import java.time.LocalDateTime;

public class CompletedTaskInfo {
    private String firebaseId;
    private LocalDateTime completedTime;

    public CompletedTaskInfo(String firebaseId, LocalDateTime completedTime) {
        this.firebaseId = firebaseId;
        this.completedTime = completedTime;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }
}