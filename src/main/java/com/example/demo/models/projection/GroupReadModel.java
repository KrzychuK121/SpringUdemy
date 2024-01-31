package com.example.demo.models.projection;

import com.example.demo.models.Task;
import com.example.demo.models.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private Integer id;
    private String description;
    /**
     * Deadline from the latests task in group.
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks()
        .stream()
        .map(Task::getDeadline)
        .max(LocalDateTime::compareTo)
        .ifPresent(date -> deadline = date);

        tasks = source.getTasks()
            .stream()
            .map(GroupTaskReadModel::new)
            .collect(Collectors.toSet());
    }

    public Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
