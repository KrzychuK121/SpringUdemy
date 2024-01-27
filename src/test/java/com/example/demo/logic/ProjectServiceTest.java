package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.TaskTemplateConfiguration;
import com.example.demo.models.*;
import com.example.demo.models.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {


    private Project projectWith(String description, Set<Long> daysToDeadline){
        // given
        var result = mock(Project.class);

        when(result.getDescription())
        .thenReturn(description);

        var projectSteps = daysToDeadline.stream()
            .map(days -> {
                var step = mock(ProjectStep.class);

                when(step.getDescription())
                        .thenReturn("foo");

                when(step.getDaysToDeadline())
                        .thenReturn(days);

                return step;
            }).collect(Collectors.toSet());

        when(result.getProjectSteps())
        .thenReturn(
            projectSteps
        );
        return result;
    }

    private ProjectRepository projectRepositoryReturning(Project whatToReturn){
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt()))
        .thenReturn(
            whatToReturn == null ?
            Optional.empty() :
            Optional.of(whatToReturn)
        );
        return mockProjectRepository;
    }

    private InMemoryGroupRepository inMemoryGroupRepository(){
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository{
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
            public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
            public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
            public TaskGroup save(TaskGroup entity) {
            if(entity.getId() == 0){
                try {
                    var field = TaskSchema.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                }catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                map.put(index, entity);
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
            public boolean existsByDoneIsFalseAndProjectId(Integer id) {
            return map.values()
                .stream()
                .filter(group -> !group.isDone())
                .anyMatch(
                    group -> group.getProject() != null &&
                    group.getProject().getId() == id
                );
        }

        @Override
        public List<TaskGroup> findByDoneIsFalseAndProjectId(Integer id) {
            return null;
        }
    }

    private TaskConfigurationProperties taskConfigurationPropertiesReturns(boolean whatToReturn){
        var mockTaskTemplateConfiguration = mock(TaskTemplateConfiguration.class);
        when(mockTaskTemplateConfiguration.isAllowMultipleTasks())
        .thenReturn(whatToReturn);
        var mockTaskConfigurationProperties = mock(TaskConfigurationProperties.class);
        when(mockTaskConfigurationProperties.getTaskTempConf())
        .thenReturn(mockTaskTemplateConfiguration);

        return mockTaskConfigurationProperties;
    }
    @Test
    @DisplayName("should throw IllegalStateException when project found, configured to allow just 1 group and the other undone group exists")
    void createGroup_projectFound_And_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        var mockProjectRepository = projectRepositoryReturning(new Project());
        // and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyInt()))
        .thenReturn(true);
        // and
        var mockConfig = taskConfigurationPropertiesReturns(false);
        // system under test
        var toTest = new ProjectService(mockProjectRepository, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        // then
        assertThat(exception)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("one undone group");
        // when + then
       /* assertThatIllegalStateException()
        .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));*/
        // lub inaczej
        /*assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> {
            toTest.createGroup(0, LocalDateTime.now());
        });*/
        // lub inaczej
        /*assertThatThrownBy(() -> {
            toTest.createGroup(0, LocalDateTime.now());
        }).isInstanceOf(IllegalStateException.class);*/
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when no project found")
    void createGroup_noProjects_throwsIllegalArgumentException(){
        // given
        var mockProjectRepository = projectRepositoryReturning(null);
        var toTest = new ProjectService(mockProjectRepository, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("No project found");
    }
    @Test
    @DisplayName("should create new group from project")
    void createGroup_projectFound_And_noMultipleGroupsConfig_And_noGroupsExists_giveGroupReadModel(){
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith(
            "bar",
            Set.of((long)-1, (long)-2)
        );
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt()))
        .thenReturn(
            Optional.of(
                project
            )
        );
        // and
        var mockTaskConfigurationProperties = taskConfigurationPropertiesReturns(true);
        // and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();

        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);

        int countBeforeCall = inMemoryGroupRepo.count();

        // system under control
        var toTest = new ProjectService(
            mockProjectRepository,
            inMemoryGroupRepo,
            serviceWithInMemRepo,
            mockTaskConfigurationProperties
        );

        // when
        GroupReadModel result = toTest.createGroup(1, today);

        // then
        assertThat(result.getDescription())
        .isEqualTo("bar");
        assertThat(result.getDeadline())
        .isEqualTo(today.minusDays(1));
        assertThat(result.getTasks())
        .allMatch(task -> task.getDescription().equals("foo"));
        // lub
        /*assertThat(result)
        .hasFieldOrPropertyWithValue("description", "bar");*/
        assertThat(countBeforeCall + 1)
        .isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(
                inMemoryGroupRepo,
                null
        );
    }
}