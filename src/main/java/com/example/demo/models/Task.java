package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table
public class Task extends TaskSchema {
    private LocalDateTime deadline;

    /*@AttributeOverrides(
        {
            @AttributeOverride(column = @Column(name = "createdOn"), name="createdOn"),
            @AttributeOverride(column = @Column(name = "updatedOn"), name="updatedOn")
        }
    )*/
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    @Embedded
    private Audit audit = new Audit();

    public Task() { super(); }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }

}
