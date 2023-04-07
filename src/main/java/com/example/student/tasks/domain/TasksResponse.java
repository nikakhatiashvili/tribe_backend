package com.example.student.tasks.domain;

import com.example.student.tasks.model.GroupTasksResponse;

import java.util.List;

public class TasksResponse {
    private List<GroupTasksResponse> tasksByGroup;

    public TasksResponse(List<GroupTasksResponse> tasksByGroup) {
        this.tasksByGroup = tasksByGroup;
    }

    public List<GroupTasksResponse> getTasksByGroup() {
        return tasksByGroup;
    }

    public void setTasksByGroup(List<GroupTasksResponse> tasksByGroup) {
        this.tasksByGroup = tasksByGroup;
    }
}