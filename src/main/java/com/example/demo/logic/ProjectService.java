package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.models.*;
import com.example.demo.models.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(
        final ProjectRepository repository,
        final TaskGroupRepository taskGroupRepository,
        final TaskConfigurationProperties config
    ) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project create(Project toSave){
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(Integer projectId, LocalDateTime deadline){
        // Get project source or throw exception
        Project projSource = repository.findById(projectId)
            .orElseThrow(
                () -> new IllegalArgumentException(
                        "No project found with given projectId"
                    )
            );
        // If can't create, throw exception
        if(
            !config.getTaskTempConf().isAllowMultipleTasks() &&
            taskGroupRepository.existsByDoneIsFalseAndProjectId(projectId)
        ){
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        // Create group with initial info
        TaskGroup toCreate = new TaskGroup();
        toCreate.setDescription(projSource.getDescription());
        toCreate.setDone(false);
        toCreate.setProject(projSource);

        // Create tasks and initialize it
        // with data inside project source (project steps)
        Set<Task> tasks = new HashSet<>();

        projSource.getProjectSteps()
            .forEach(projStep -> {
                var task = new Task();
                task.setDone(false);
                task.setDescription(projStep.getDescription());

                var daysToDeadline = projStep.getDaysToDeadline();

                task.setDeadline(
                    deadline.plusDays(daysToDeadline)
                );
                tasks.add(task);
            });

        toCreate.setTasks(tasks);

        // Return group of tasks converted to DTO
        return new GroupReadModel(taskGroupRepository.save(toCreate));
    }

}
