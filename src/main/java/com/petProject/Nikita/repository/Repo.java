package com.petProject.Nikita.repository;

import com.petProject.Nikita.Model.Status;
import com.petProject.Nikita.Model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;
import java.util.List;

@Repository
public interface Repo extends JpaRepository<TaskModel, Long> {
    @Query("SELECT t FROM TaskModel t WHERE t.status = :status")
    List<TaskModel> findByStatus(@Param("status") Status status);
}
