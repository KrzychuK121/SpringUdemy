package com.example.demo.controllers;

import com.example.demo.logic.ProjectService;
import com.example.demo.models.Project;
import com.example.demo.models.ProjectStep;
import com.example.demo.models.projection.ProjectWriteModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(
        final ProjectService service
    ) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
        @ModelAttribute("project") @Valid ProjectWriteModel current,
        BindingResult bindResult,
        Model model
    ){
        if(bindResult.hasErrors()){
            return "projects";
        }
        service.create(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(
        @ModelAttribute("project") ProjectWriteModel current,
        Model model,
        @PathVariable int id,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ){
        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Dodano grupę!");
        } catch(IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }
}
