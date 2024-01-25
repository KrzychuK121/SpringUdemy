package com.example.demo.adapter;

import com.example.demo.models.TaskGroup;
import com.example.demo.models.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("SELECT DISTINCT g FROM TaskGroup g JOIN FETCH g.tasks")
    List<TaskGroup> findAll();
}
