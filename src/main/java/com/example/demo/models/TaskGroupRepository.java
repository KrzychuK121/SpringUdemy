package com.example.demo.models;


import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();
    Optional<TaskGroup> findById(Integer id);
    TaskGroup save(TaskGroup entity);
    List<TaskGroup> findByDoneIsFalseAndProjectId(Integer id);

}
