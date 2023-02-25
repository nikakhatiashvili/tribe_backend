package com.example.student.tasks;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table
public class TribeTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String name;

    private Long groupId;

    @NotNull
    @Size(min = 10, max = 100)
    private String description;

    @Transient
    @Email(message = "Invalid email address")
    private String email;

    @ElementCollection
    private List<String> assignedTo;

    private LocalDateTime resetTime;

    @ElementCollection
    private Map<String, LocalDateTime> completedTodayBy = new HashMap<>();

    private LocalDateTime dateCompleted;

    private String comment;
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"description\":\"" + description + "\"" +
                ", \"assignedTo\":\"" + assignedTo + "\"" +
                ", \"groupId\":\"" + groupId + "\"" +
                ", \"resetTime\":\"" + resetTime + "\"" +
                ", \"dateCompleted\":\"" + dateCompleted + "\"" +
                ", \"comment\":\"" + comment + "\"" +
                "}";
    }


    public TribeTask(String name, String description, Long groupId) {
        this.name = name;
        this.description = description;
        this.groupId = groupId;
    }
    public TribeTask(String name, String description, Long groupId, long id) {
        this.name = name;
        this.description = description;
        this.groupId = groupId;
        this.id = id;
    }

    public TribeTask() {}

    public Map<String, LocalDateTime> getCompletedTodayBy() {
        return completedTodayBy;
    }

    public void setCompletedTodayBy(Map<String, LocalDateTime> completedTodayBy) {
        this.completedTodayBy = completedTodayBy;
    }

    public void addCompletedTodayBy(String firebaseId, LocalDateTime dateCompleted) {
        if (completedTodayBy == null) {
            completedTodayBy = new HashMap<>();
        }
        completedTodayBy.put(firebaseId, dateCompleted);
    }

    public boolean isCompletedTodayBy(String firebaseId) {
        return completedTodayBy != null && completedTodayBy.containsKey(firebaseId);
    }

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public List<String> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<String> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getResetTime() {
        return resetTime;
    }

    public void setResetTime(LocalDateTime resetTime) {
        this.resetTime = resetTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
