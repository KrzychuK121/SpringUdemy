package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.models.ProjectRepository;
import com.example.demo.models.TaskGroupRepository;
import com.example.demo.models.TasksRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(
        final ProjectRepository repository,
        final TaskGroupRepository taskGroupRepository,
        final TaskGroupService taskGroupService,
        final TaskConfigurationProperties config
    ){
        return new ProjectService(
            repository,
            taskGroupRepository,
            taskGroupService,
            config
        );
    }

    @Bean
    @RequestScope
    TaskGroupService taskGroupService(
        final TaskGroupRepository repository,
        final TasksRepository tasksRepository
    ){
        return new TaskGroupService(
            repository,
            tasksRepository
        );
    }
}
