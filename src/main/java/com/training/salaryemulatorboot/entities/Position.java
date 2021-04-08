package com.training.salaryemulatorboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Table(name = "positions")
// TODO tasks
// дерево иерархии позиций
// сохранять историю позиции Jr -> Middle -> Senior
// когда позиция была занята?
// как поменялось количество подчененных изменилось за год
// * аудит, вьюха для поиска
// Intercepter for incoming value
public class Position {
    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Please provide a name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_position_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position managerPosition;
}