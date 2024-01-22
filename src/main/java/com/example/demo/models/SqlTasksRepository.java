package com.example.demo.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
@Repository
interface SqlTasksRepository extends TasksRepository, JpaRepository<Task, Integer> {

}
