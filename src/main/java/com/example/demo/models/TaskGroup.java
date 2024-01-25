package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends TaskSchema {
    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "group"
    ) // default fetch = FetchType.LAZY
    private Set<Task> tasks;
    @Embedded
    private Audit audit = new Audit();

    public TaskGroup() { }

    public Set<Task> getTasks() {
        return tasks;
    }

    void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
