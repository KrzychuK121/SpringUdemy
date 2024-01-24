package com.example.demo.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
@Repository
interface SqlTasksRepository extends TasksRepository, JpaRepository<Task, Integer> {
// Inna opcja
//    @Override
//    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASK WHERE id = ?1")
//    boolean existsById(Integer id);

    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM task WHERE id = :identyfikator")
    boolean existsById(@Param("identyfikator") Integer id);
}
