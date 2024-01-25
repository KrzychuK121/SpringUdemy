package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "The description must be not null!")
    private String description;
    @Column(name = "days_to_deadline")
    private long daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectStep() { }

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

    public long getDaysToDeadline() {
        return daysToDeadline;
    }

    public void setDaysToDeadline(long daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    Project getProject() {
        return project;
    }

    void setProject(Project project) {
        this.project = project;
    }
}
