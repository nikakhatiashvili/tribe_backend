package com.example.student.tasks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class TribeTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min = 3, max = 40)
    private String name;
    @Transient
    private boolean completed;
    private Long groupId;
    @NotNull
    @Size(min = 10, max = 100)
    private String description;
    @Email(message = "Invalid email address")
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> assignedTo;
    private Boolean forAll;
    private LocalDateTime resetTime;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> completedTodayBy = new HashSet<>();
    private LocalDateTime dateCompleted;
    private String comment;

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

    public TribeTask() {
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Boolean getForAll() {
        return forAll;
    }

    public void setForAll(Boolean forAll) {
        this.forAll = forAll;
    }

    public Set<String> getCompletedTodayBy() {
        return completedTodayBy;
    }

    public void setCompletedTodayBy(Set<String> completedTodayBy) {
        this.completedTodayBy = completedTodayBy;
    }

    public void addCompletedTodayBy(String firebaseId) {
        completedTodayBy.add(firebaseId);
    }

    public void removeCompletedTodayBy(String firebaseId) {
        completedTodayBy.remove(firebaseId);
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
