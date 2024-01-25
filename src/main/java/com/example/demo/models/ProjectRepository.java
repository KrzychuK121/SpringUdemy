package com.example.demo.models;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();
    Optional<Project> findById(Integer integer);
    Project save(Project entity);
}
