package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Description must not be null")
    private String description;
    private boolean done;
    private LocalDateTime deadline;

    /*@AttributeOverrides(
        {
            @AttributeOverride(column = @Column(name = "createdOn"), name="createdOn"),
            @AttributeOverride(column = @Column(name = "updatedOn"), name="updatedOn")
        }
    )*/
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn
    private TaskGroup group;

    public Task() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
    }

}
