package com.example.demo.controllers;

import com.example.demo.logic.TaskGroupService;
import com.example.demo.models.TasksRepository;
import com.example.demo.models.projection.GroupReadModel;
import com.example.demo.models.projection.GroupTaskReadModel;
import com.example.demo.models.projection.GroupWriteModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private TaskGroupService service;
    private TasksRepository tasksRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    public TaskGroupController(
        final TaskGroupService service,
        final TasksRepository tasksRepository
    ) {
        this.service = service;
        this.tasksRepository = tasksRepository;
    }


    @GetMapping()
    ResponseEntity<List<GroupReadModel>> getAll(){
        logger.warn("Exposing all groups!");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<GroupTaskReadModel>> getAllGroupTasks(@PathVariable("id") Integer groupId){
        ResponseEntity<List<GroupTaskReadModel>> response = null;
        try {
            response = ResponseEntity.ok(service.getAllGroupTasks(groupId));
        } catch(IllegalArgumentException e) {
            response = ResponseEntity.notFound().build();
        } finally {
            return response;
        }
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid final GroupWriteModel source){
        var result = service.createGroup(source);
        return ResponseEntity.created(
            URI.create("localhost:8080/groups/" + result.getId())
        ).body(result);
    }

    // Moja implementacja
//    @PatchMapping("/{groupId}")
//    ResponseEntity<?> toggleGroup(@PathVariable Integer groupId){
//        ResponseEntity<?> response = null;
//        try {
//            service.toggleGroup(groupId);
//            response = ResponseEntity.noContent().build();
//        } catch(IllegalStateException e) {
//            response = ResponseEntity.badRequest().build();
//        } catch(IllegalArgumentException e){
//            response = ResponseEntity.notFound().build();
//        } finally {
//            return response;
//        }
//    }

    @PatchMapping("/{groupId}")
    ResponseEntity<?> toggleGroup(@PathVariable Integer groupId){
        service.toggleGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
