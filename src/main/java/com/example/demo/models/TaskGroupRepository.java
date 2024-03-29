package com.example.demo.models;


import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();
    Optional<TaskGroup> findById(Integer id);
    TaskGroup save(TaskGroup entity);
    boolean existsByDoneIsFalseAndProjectId(Integer id);
    List<TaskGroup> findByDoneIsFalseAndProjectId(Integer id);

}
