package com.example.demo.models;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TasksRepository {
    List<Task> findAll();
    Page<Task> findAll(Pageable page);
    Optional<Task> findById(Integer id);
    Optional<List<Task>> findAllByGroupId(Integer groupId);
    Optional<List<Task>> findTasksDue(@Param("date") LocalDateTime date);
    List<Task> findByDone(@Param("isDone") boolean done);

    Task save(Task entity);

    boolean existsById(Integer id);
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

}
