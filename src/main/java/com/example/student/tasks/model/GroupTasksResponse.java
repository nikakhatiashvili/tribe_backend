package com.example.student.tasks.model;

import com.example.student.tasks.TribeTask;

import java.util.List;

public class GroupTasksResponse {
    private String groupName;
    private List<TribeTask> tasks;

    public GroupTasksResponse(String groupName, List<TribeTask> tasks) {
        this.groupName = groupName;
        this.tasks = tasks;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TribeTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<TribeTask> tasks) {
        this.tasks = tasks;
    }
}
