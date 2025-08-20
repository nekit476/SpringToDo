package com.petProject.Nikita.Service;

import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import com.petProject.Nikita.repository.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class MainService implements ServiceInterface {

    private final Repo repo;

    @Autowired
    public MainService(Repo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskModel> returnAllTasks() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public void addNewTask(TaskModel taskModel) {
        repo.save(taskModel);
    }
    @Override
    @Transactional
    public void deleteTask(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public void editTask(Long id, Map<String, Object> update) {
        Optional<TaskModel> task = repo.findById(id);
        if (task.isEmpty()) return;

        TaskModel editTask = task.get();

        if (update.containsKey("name")) {
            String name = update.get("name").toString();
            editTask.setName(name);
        }
        if (update.containsKey("description")) {
            String description = update.get("description").toString();
            editTask.setDescription(description);
        }
        if (update.containsKey("status")) {
            String status = update.get("status").toString();
            editTask.setStatus(Status.valueOf(status));
        }
        if (update.containsKey("deadline")) {
            String deadlineStr = update.get("deadline").toString();
            LocalDate deadline = LocalDate.parse(deadlineStr);
            editTask.setTaskDeadline(deadline);
        }

        repo.save(editTask);
    }



    @Override
    @Transactional(readOnly = true)
    public List<TaskModel> sortTasks(OrderSortType method) {
        List<TaskModel> listOfTasks = repo.findAll();
        return listOfTasks.stream()
                .sorted(createCompare(method))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskModel> filterTasks(Status status) {
        return repo.findByStatus(status);
    }

    private Comparator<TaskModel> createCompare(OrderSortType method) {
        List<Status> customOrder = List.of(
                Status.TODO,
                Status.IN_PROGRESS,
                Status.DONE
        );
        if(method.equals(OrderSortType.BY_STATUS)) {

            return Comparator
                    .comparing((TaskModel task) -> customOrder.indexOf(task.getStatus()))
                    .thenComparing(TaskModel::getTaskDeadline);

        } else if(method.equals(OrderSortType.BY_TIME)) {
            return Comparator
                    .comparing(TaskModel::getTaskDeadline)
                    .thenComparing((TaskModel task) -> customOrder.indexOf(task.getStatus()));
        }
        return Comparator.comparing(TaskModel::getName);
    }



}
