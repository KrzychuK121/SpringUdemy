package com.example.demo.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends TaskSchema {
    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "group"
    ) // default fetch = FetchType.LAZY
    private Set<Task> tasks;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Embedded
    private Audit audit = new Audit();

    public TaskGroup() { }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
