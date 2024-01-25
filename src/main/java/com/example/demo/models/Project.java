package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "The description must be not null!")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> taskGroups;
    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "project"
    )
    private Set<ProjectStep> projectSteps;

    public Project() { }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    void setTaskGroups(Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    Set<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    void setProjectSteps(Set<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }
}
