package com.example.demo.logic;

import com.example.demo.models.Project;
import com.example.demo.models.TaskGroup;
import com.example.demo.models.TaskGroupRepository;
import com.example.demo.models.TasksRepository;
import com.example.demo.models.projection.GroupReadModel;
import com.example.demo.models.projection.GroupTaskReadModel;
import com.example.demo.models.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TasksRepository tasksRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupService.class);

    TaskGroupService(
        final TaskGroupRepository repository,
        final TasksRepository tasksRepository
    ){
        this.repository = repository;
        this.tasksRepository = tasksRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source){
        return createGroup(source, null);
    }

    GroupReadModel createGroup(
        final GroupWriteModel source,
        final Project sourceProject
    ){
        TaskGroup result = repository.save(source.toGroup(sourceProject));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll()
            .stream()
            .map(GroupReadModel::new)
            .collect(Collectors.toList());
    }

    public List<GroupTaskReadModel> getAllGroupTasks(Integer groupId){
        var result = tasksRepository.findAllByGroupId(groupId)
            .orElseThrow(
                () -> new IllegalArgumentException("Group with given id has no tasks")
            );

        var tasksReadModel = new ArrayList<GroupTaskReadModel>(result.size());
        result.forEach(
            task -> tasksReadModel.add(new GroupTaskReadModel(task))
        );

        return tasksReadModel;
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
