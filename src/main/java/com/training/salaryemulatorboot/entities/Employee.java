package com.training.salaryemulatorboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "position"})
    private Position position;

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    private String name;

    @Column(name = "salary_amount", nullable = false)
    @Min(value = 0, message = "salary_amount should be more than 0")
    private BigDecimal salaryAmount;

    @Column(name = "salary_currency", nullable = false)
    private String salaryCurrency;
}