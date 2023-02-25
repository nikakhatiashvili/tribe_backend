package com.example.student.tasks.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "completed_tasks")
public class CompletedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "comment")
    private String comment;

    public CompletedTask() {}

    public CompletedTask(String userId, Long taskId, LocalDateTime completedAt, String comment) {
        this.userId = userId;
        this.taskId = taskId;
        this.completedAt = completedAt;
        this.comment = comment;
    }


}


