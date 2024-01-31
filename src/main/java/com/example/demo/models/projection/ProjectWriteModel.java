package com.example.demo.models.projection;

import com.example.demo.models.Project;
import com.example.demo.models.ProjectStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "The description must be not null!")
    private String description;
    @Valid
    private List<ProjectStep> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();

        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setProjectSteps(new HashSet<>(steps));

        return result;
    }
}
