package com.petProject.Nikita;

import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import com.petProject.Nikita.Service.MainService;
import com.petProject.Nikita.Service.OrderSortType;
import com.petProject.Nikita.repository.Repo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class MainServiceTests {

    @Autowired
    private MainService mainService;

    @MockitoBean
    private Repo repo;

    @Test
    public void returnAllTasks_test() {
        List<TaskModel> mockList = List.of(new TaskModel("mockTask", "description", Status.TODO, LocalDate.parse("2030-08-19")));

        Mockito.when(repo.findAll()).thenReturn(mockList);

        List<TaskModel> result = mainService.returnAllTasks();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("mockTask", result.getFirst().getName());
    }
    @Test
    public void addNewTask_test() {
        TaskModel testTask = new TaskModel("mockTaskSecond", "descriptionSecond", Status.TODO, LocalDate.parse("2035-08-19"));

        mainService.addNewTask(testTask);

        Mockito.verify(repo, Mockito.times(1)).save(testTask);
    }

    @Test
    public void addNewTask_test_withList() {
        List<TaskModel> addedTasks = new ArrayList<>();

        Mockito.when(repo.save(Mockito.any(TaskModel.class)))
                .thenAnswer(answer -> {
                    TaskModel task = answer.getArgument(0);
                    addedTasks.add(task);
                    return task;
                });

        TaskModel task1 = new TaskModel("mockTask", "description", Status.TODO, LocalDate.parse("2030-08-19"));
        TaskModel task2 = new TaskModel("mockTaskSecond", "descriptionSecond", Status.TODO, LocalDate.parse("2035-08-19"));

        mainService.addNewTask(task1);
        mainService.addNewTask(task2);

        Assertions.assertEquals(2, addedTasks.size());
        Assertions.assertEquals("mockTaskSecond", addedTasks.get(1).getName());
    }



    @Test
    public void deleteTask_test_withList() {
        TaskModel testTaskFirst = new TaskModel("firstTask", "firstDescription", Status.TODO, LocalDate.parse("2030-09-11"));
        testTaskFirst.setId(1L);

        TaskModel testTaskSecond = new TaskModel("secondTask", "secondDescription", Status.TODO, LocalDate.parse("2030-09-11"));
        testTaskSecond.setId(2L);

        List<TaskModel> testList = new ArrayList<>(List.of(
                testTaskFirst,
                testTaskSecond
        ));

        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            testList.removeIf(task -> task.getId().equals(id));
            return null;
        }).when(repo).deleteById(Mockito.anyLong());

        mainService.deleteTask(1L);

        Assertions.assertEquals(1, testList.size());

    }
    @Test
    public void editTask_test() {
        TaskModel testTask = new TaskModel("firstTask", "firstDescription", Status.TODO, LocalDate.parse("2030-09-11"));
        testTask.setId(1L);
        Optional<TaskModel> testTaskOptional = Optional.of(testTask);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", "firstTaskEdit");

        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(testTaskOptional);

        mainService.editTask(1L, updateMap);

        Assertions.assertEquals("firstTaskEdit", testTask.getName());

        Mockito.verify(repo).save(testTask);
    }

    @Test
    public void sortTasks_test_sort_by_status () {
        List<TaskModel> testTask = generateTestTasks();
        List<TaskModel> testTaskSortedByStatus = List.of(
                new TaskModel("AlphaTask", "Description 1", Status.TODO, LocalDate.parse("2030-09-10")),
                new TaskModel("DeltaTask", "Description 4", Status.TODO, LocalDate.parse("2030-09-13")),
                new TaskModel("EpsilonTask", "Description 5", Status.IN_PROGRESS, LocalDate.parse("2030-09-09")),
                new TaskModel("BetaTask", "Description 2", Status.IN_PROGRESS, LocalDate.parse("2030-09-12")),
                new TaskModel("GammaTask", "Description 3", Status.DONE, LocalDate.parse("2030-09-11"))
        );
        Mockito.when(repo.findAll()).thenReturn(testTask);

        List<TaskModel> sortedList =  mainService.sortTasks(OrderSortType.BY_STATUS);

        Assertions.assertEquals(testTaskSortedByStatus, sortedList);

    }

    @Test
    public void sortTasks_test_sort_by_deadline () {
        List<TaskModel> testTask = generateTestTasks();
        List<TaskModel> sortedByDeadline = List.of(
                new TaskModel("EpsilonTask", "Description 5", Status.IN_PROGRESS, LocalDate.parse("2030-09-09")),
                new TaskModel("AlphaTask", "Description 1", Status.TODO, LocalDate.parse("2030-09-10")),
                new TaskModel("GammaTask", "Description 3", Status.DONE, LocalDate.parse("2030-09-11")),
                new TaskModel("BetaTask", "Description 2", Status.IN_PROGRESS, LocalDate.parse("2030-09-12")),
                new TaskModel("DeltaTask", "Description 4", Status.TODO, LocalDate.parse("2030-09-13"))
        );

        Mockito.when(repo.findAll()).thenReturn(testTask);

        List<TaskModel> sortedList =  mainService.sortTasks(OrderSortType.BY_TIME);

        Assertions.assertEquals(sortedByDeadline, sortedList);

    }
    @Test
    public void filterTasks_test_status_TODO () {
        List<TaskModel> testListOfTasks = generateTestTasks();
        List<TaskModel> testListOfTasksFilterByTODO = List.of(
                new TaskModel("AlphaTask", "Description 1", Status.TODO, LocalDate.parse("2030-09-10")),
                new TaskModel("DeltaTask", "Description 4", Status.TODO, LocalDate.parse("2030-09-13"))
        );

        Mockito.when(repo.findByStatus(Mockito.any())).thenReturn(testListOfTasksFilterByTODO);

        List<TaskModel> resultList = mainService.filterTasks(Status.TODO);

        Assertions.assertEquals(resultList, testListOfTasksFilterByTODO);
    }



    private List<TaskModel> generateTestTasks() {
        return List.of(
                new TaskModel("AlphaTask", "Description 1", Status.TODO, LocalDate.parse("2030-09-10")),
                new TaskModel("BetaTask", "Description 2", Status.IN_PROGRESS, LocalDate.parse("2030-09-12")),
                new TaskModel("GammaTask", "Description 3", Status.DONE, LocalDate.parse("2030-09-11")),
                new TaskModel("DeltaTask", "Description 4", Status.TODO, LocalDate.parse("2030-09-13")),
                new TaskModel("EpsilonTask", "Description 5", Status.IN_PROGRESS, LocalDate.parse("2030-09-09"))
        );
    }

}
