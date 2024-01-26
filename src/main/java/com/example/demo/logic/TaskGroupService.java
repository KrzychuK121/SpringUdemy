package com.example.demo.logic;

import com.example.demo.models.TaskGroup;
import com.example.demo.models.TaskGroupRepository;
import com.example.demo.models.TasksRepository;
import com.example.demo.models.projection.GroupReadModel;
import com.example.demo.models.projection.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TasksRepository tasksRepository;

    TaskGroupService(
        final TaskGroupRepository repository,
        final TasksRepository tasksRepository
    ){
        this.repository = repository;
        this.tasksRepository = tasksRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source){
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll()
            .stream()
            .map(GroupReadModel::new)
            .collect(Collectors.toList());
    }

    public void toggleGroup(Integer groupId){
        if(tasksRepository.existsByDoneIsFalseAndGroupId(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first.");
        }
        TaskGroup result = repository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
