package com.petProject.Nikita.Service;

import aj.org.objectweb.asm.commons.TryCatchBlockSorter;
import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Map;
@Service("MainServiceProxy")
public class MainServiceProxy implements ServiceInterface {

    private final ServiceInterface serviceInterface;

    @Autowired
    public MainServiceProxy(@Qualifier("mainService") ServiceInterface serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public List<TaskModel> returnAllTasks() {
        return serviceInterface.returnAllTasks();
    }

    @Override
    public void addNewTask(TaskModel taskModel) {
        try {
            serviceInterface.addNewTask(taskModel);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Переданы некорректные данные задачи", e);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Нарушена целостность данных", e);

        } catch (TransactionSystemException e) {
            throw new RuntimeException("Ошибка при выполнении транзакции", e);

        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("База данных недоступна", e);

        } catch (NullPointerException e) {
            throw new RuntimeException("Сервис или модель задачи не инициализированы", e);

        } catch (Exception e) {
            throw new RuntimeException("Возникла неизвестная ошибка", e);
        }

    }

    @Override
    public void deleteTask(Long id) {
        try {
        serviceInterface.deleteTask(id);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Передан некорректный идентификатор задачи", e);

        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Задача с таким идентификатором не найдена", e);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Невозможно удалить задачу: нарушена целостность данных", e);

        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("База данных недоступна", e);

        } catch (TransactionSystemException e) {
            throw new RuntimeException("Ошибка при выполнении транзакции", e);

        } catch (Exception e) {
            throw new RuntimeException("Возникла неизвестная ошибка при удалении задачи", e);
        }
    }

    @Override
    public void editTask(Long id, Map<String, Object> update) {
        try {
            serviceInterface.editTask(id, update);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Переданы некорректные параметры для обновления задачи", e);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Задача с таким идентификатором не найдена", e);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Невозможно обновить задачу: нарушена целостность данных", e);

        } catch (TransactionSystemException e) {
            throw new RuntimeException("Ошибка при выполнении транзакции", e);

        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("База данных недоступна", e);

        } catch (Exception e) {
            throw new RuntimeException("Возникла неизвестная ошибка при обновлении задачи", e);
        }
    }

    @Override
    public List<TaskModel> sortTasks(OrderSortType method) {

        try {
            return serviceInterface.sortTasks(method);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Передан некорректный метод сортировки", e);

        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("База данных недоступна", e);

        } catch (TransactionSystemException e) {
            throw new RuntimeException("Ошибка при выполнении транзакции при сортировке", e);

        } catch (JpaSystemException e) {
            throw new RuntimeException("Ошибка уровня JPA при сортировке задач", e);

        } catch (Exception e) {
            throw new RuntimeException("Возникла неизвестная ошибка при сортировке задач", e);
        }
    }

    @Override
    public List<TaskModel> filterTasks(Status status) {
        try {
            return serviceInterface.filterTasks(status);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Передан некорректный статус для фильтрации", e);

        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            throw new RuntimeException("База данных недоступна", e);

        } catch (org.springframework.transaction.TransactionSystemException e) {
            throw new RuntimeException("Ошибка при выполнении транзакции при фильтрации", e);

        } catch (org.springframework.orm.jpa.JpaSystemException |
                 jakarta.persistence.PersistenceException e) {
            throw new RuntimeException("Ошибка уровня JPA при фильтрации задач", e);

        } catch (Exception e) {
            throw new RuntimeException("Возникла неизвестная ошибка при фильтрации задач", e);
        }

    }
}
