package com.example.demo.adapter;

import com.example.demo.models.Project;
import com.example.demo.models.ProjectRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {
    @Override
    @Query("SELECT DISTINCT p FROM Project p JOIN FETCH p.projectSteps")
    List<Project> findAll();
}
