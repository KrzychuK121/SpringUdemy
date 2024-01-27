package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.models.TasksRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController //@Controller
public class TasksController {
    private static final Logger logger = LoggerFactory.getLogger(TasksController.class);

    private final TasksRepository repository;

    // Qualifier -> wskazuje na to z jakiej definicji klasy korzystamy przy wstrzykiwaniu
    // Lazy -> bean zostaje dodany dopiero gdy będzie używany
    public TasksController(
            /*@Qualifier("sqlTasksRepository")*/
            /*@Lazy*/
            final TasksRepository repository
    ){
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks", params = {"!sort", "!page", "!size"})
    public ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    public ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pager");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks/{id}")
    public ResponseEntity<Task> readTaskById(@PathVariable int id){
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task)) // ResponseEntity::ok
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasks")
    ResponseEntity<Task> postTask(@RequestBody @Valid Task toCreate){
        Task created = repository.save(toCreate);
        return ResponseEntity.created(
            URI.create("localhost:8080/tasks/" + created.getId())
        ).body(created);
    }

    //@Transactional
    @RequestMapping(method = RequestMethod.PUT, value = "/tasks/{id}")
    ResponseEntity<?> updateTasks(@PathVariable Integer id, @RequestBody @Valid Task toUpdate){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id)
            .ifPresent(task -> {
                task.updateFrom(toUpdate);
                repository.save(task);
            });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/tasks/{id}")
    ResponseEntity<?> toggleTasks(@PathVariable Integer id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}
