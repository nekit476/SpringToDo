package com.petProject.Nikita.Service;

import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface ServiceInterface {

    List<TaskModel> returnAllTasks();

    void addNewTask(TaskModel taskModel);

    void deleteTask(Long id);

    void editTask(Long id, Map<String, Object> update);

    List<TaskModel> sortTasks(OrderSortType method);

    List<TaskModel> filterTasks(Status status);

}
