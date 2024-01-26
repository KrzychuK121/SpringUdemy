package com.example.demo.logic;

import com.example.demo.models.TaskGroup;
import com.example.demo.models.TaskGroupRepository;
import com.example.demo.models.TasksRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    private TasksRepository taskRepositoryHasUndoneTasks(boolean hasUndoneTasks){
        var mock = mock(TasksRepository.class);

        when(mock.existsByDoneIsFalseAndGroupId(anyInt()))
        .thenReturn(hasUndoneTasks);

        return mock;
    }

    private TaskGroupRepository taskGroupRepository(boolean findsById, TaskGroup toSave){
        var mock = mock(TaskGroupRepository.class);

        when(mock.findById(anyInt()))
        .thenReturn(
            findsById ?
            Optional.of(toSave) :
            Optional.empty()
        );

        return mock;
    }

    @Test
    @DisplayName("exists is true, should throw IllegalStateException")
    void toggleGroup_existsIsTrue_throwsIllegalStateException() {
        // given
        var mockTasksRepository = taskRepositoryHasUndoneTasks(true);

        // system under control
        var taskGroupService = new TaskGroupService(null, mockTasksRepository);

        // when
        var exception = catchThrowable(
            () -> taskGroupService.toggleGroup(0)
        );

        // then
        assertThat(exception)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("find returns empty optional, should throw IllegalArgumentException")
    void toggleGroup_findReturnsEmptyOptional_throwsIllegalArgumentException(){
        // given
        var mockTaskRepository = taskRepositoryHasUndoneTasks(false);
        // and
        var mockTaskGroupRepository = taskGroupRepository(false, null);

        // system under control
        TaskGroupService toTest = new TaskGroupService(
            mockTaskGroupRepository,
            mockTaskRepository
        );

        // when
        var exception = catchThrowable(
            () -> toTest.toggleGroup(0)
        );

        // then
        assertThat(exception)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("configuration is okay without any exception, should give saved TaskGroup")
    void toggleGroup_configurationOk_givesSavedTaskGroup(){
        // given
        var mockTasksRepository = taskRepositoryHasUndoneTasks(false);
        // and
        var taskGroupToToggle = new TaskGroup();//mock(TaskGroup.class);

        var description = "description";
        taskGroupToToggle.setDescription(description);
        var isDone = true;
        taskGroupToToggle.setDone(isDone);

        var mockTaskGroupRepository = taskGroupRepository(true, taskGroupToToggle);

        // system under control
        TaskGroupService toTest = new TaskGroupService(
            mockTaskGroupRepository,
            mockTasksRepository
        );

        // when
        toTest.toggleGroup(0);

        // then
        assertThat(taskGroupToToggle.getDescription())
        .isEqualTo(description);

        assertThat(taskGroupToToggle.isDone())
        .isEqualTo(!isDone);

    }

}