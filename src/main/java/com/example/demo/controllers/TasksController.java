package com.example.demo.controllers;

import com.example.demo.logic.TaskService;
import com.example.demo.models.Task;
import com.example.demo.models.TasksRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController //@Controller
@RequestMapping("/tasks")
public class TasksController {
    private static final Logger logger = LoggerFactory.getLogger(TasksController.class);

    private final TasksRepository repository;
    private final TaskService service;

    // Qualifier -> wskazuje na to z jakiej definicji klasy korzystamy przy wstrzykiwaniu
    // Lazy -> bean zostaje dodany dopiero gdy będzie używany
    public TasksController(
            /*@Qualifier("sqlTasksRepository")*/
            /*@Lazy*/
            final TasksRepository repository,
            final TaskService service
    ){
        this.repository = repository;
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size", "!isDone"})
    public ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pager");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
    //-------------------------
    // Z kursu
    @RequestMapping(method = RequestMethod.GET, value = "/search/done")
    public ResponseEntity<List<Task>> readDoneTasks(
        @RequestParam(defaultValue = "true") boolean state
    ){
        logger.info("All done tasks displayed");
        return ResponseEntity.ok(repository.findByDone(state));
    }

    // Moje
    @RequestMapping(method = RequestMethod.GET, params = { "isDone" })
    public ResponseEntity<List<Task>> readAllDoneTasks(boolean isDone){
        logger.info("All done tasks displayed");
        return ResponseEntity.ok(repository.findByDone(isDone));
    }
    //-------------------------

    @RequestMapping(method = RequestMethod.GET, value = "/search/today")
    public ResponseEntity<List<Task>> readTodaysTasks(){
        try {
            return ResponseEntity.ok(service.getTodaysAndOutstandingTasks());
        } catch(IllegalStateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Task> readTaskById(@PathVariable int id){
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task)) // ResponseEntity::ok
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> postTask(@RequestBody @Valid Task toCreate){
        Task created = repository.save(toCreate);
        return ResponseEntity.created(
            URI.create("localhost:8080/tasks/" + created.getId())
        ).body(created);
    }

    //@Transactional
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    ResponseEntity<?> toggleTasks(@PathVariable Integer id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}
