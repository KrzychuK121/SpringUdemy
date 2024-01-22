package com.example.demo.models;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TasksRepository {
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    Page<Task> findAll(Pageable page);
    List<Task> findByDone(@Param("isDone") boolean done);

    Task save(Task entity);

    boolean existsById(Integer id);
}
