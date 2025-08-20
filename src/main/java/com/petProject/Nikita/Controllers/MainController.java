package com.petProject.Nikita.Controllers;

import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import com.petProject.Nikita.Service.MainService;
import com.petProject.Nikita.Service.OrderSortType;
import com.petProject.Nikita.Service.ServiceInterface;
import com.petProject.Nikita.repository.Repo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class MainController  {




    private final ServiceInterface serviceInterface;


    @Autowired
    public MainController(@Qualifier("MainServiceProxy") ServiceInterface serviceInterface) {
        this.serviceInterface = serviceInterface;

    }




    @GetMapping("/all-tasks")
    public List<TaskModel> returnAllTasks() {
        return serviceInterface.returnAllTasks();
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam() String name,
                          @RequestParam() String description,
                          @RequestParam() String deadLine
                          ){
        LocalDate deadline = LocalDate.parse(deadLine);
        serviceInterface.addNewTask(new TaskModel(name, description, Status.TODO, deadline));
        return "TaskAdded";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable() Long id){
        serviceInterface.deleteTask(id);
        return "TaskDelete";
    }

    @PatchMapping("/patchTask/{id}")
    public String patchTask(@PathVariable() Long id,
                            @RequestBody Map<String, Object> update) {
        serviceInterface.editTask(id, update);

        return "TaskPatched";
    }

    @PostMapping("/sortedTasks")
    public List<TaskModel> sortedTasks(@RequestParam() String orderSortType) {
        return serviceInterface.sortTasks(OrderSortType.valueOf(orderSortType));
    }

    @PostMapping("/filterTasks")
    public List<TaskModel> filteredTasks(@RequestParam() String status) {

        return serviceInterface.filterTasks(Status.valueOf(status));
    }



}
