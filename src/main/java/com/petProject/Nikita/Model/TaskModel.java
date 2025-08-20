package com.petProject.Nikita.Model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TaskModel implements TaskModelInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Status status;
    private LocalDate taskDeadline;


    public TaskModel(String name, String description, Status status, LocalDate taskDeadline) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskDeadline = taskDeadline;
    }







}
