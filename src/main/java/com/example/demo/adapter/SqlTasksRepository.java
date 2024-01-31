package com.example.demo.adapter;

import ch.qos.logback.core.joran.spi.DefaultClass;
import com.example.demo.models.Task;
import com.example.demo.models.TasksRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
@Repository
interface SqlTasksRepository extends TasksRepository, JpaRepository<Task, Integer> {
    @Override
    Optional<List<Task>> findAllByGroupId(Integer groupId);

    @Override
    @Query(value = """
        SELECT t FROM Task t
        WHERE t.done = false AND (
            t.deadline = null OR
            t.deadline = :date OR
            t.deadline < :date
        )
    """)
    Optional<List<Task>> findTasksDue(@Param("date") LocalDateTime date);
// Inna opcja
//    @Override
//    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASK WHERE id = ?1")
//    boolean existsById(Integer id);

    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM task WHERE id = :identyfikator")
    boolean existsById(@Param("identyfikator") Integer id);
    @Override
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

}
