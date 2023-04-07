package com.example.student.tasks.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table
public class TaskCompletionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long taskId;

    private String username;

    private Long groupId;

    private String strDate;

    private Long userId;

    private Date date;

    private String message;

    public TaskCompletionMessage() {
    }

    public TaskCompletionMessage(long taskId, Long userId, String username, Date date, String message, Long groupId) {
        this.taskId = taskId;
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.message = message;
        this.groupId = groupId;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.strDate = dateFormat.format(date);
    }

    public long getId() {
        return id;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}