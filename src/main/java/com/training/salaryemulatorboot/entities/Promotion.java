package com.training.salaryemulatorboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    @Column(name = "old_salary_amount")
    @Min(value = 0, message = "old_salary_amount should be more than 0")
    private BigDecimal oldSalaryAmount;

    @Column(name = "new_salary_amount", nullable = false)
    @Min(value = 0, message = "new_salary_amount should be more than 0")
    private BigDecimal newSalaryAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_position_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position oldPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_position_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position newPosition;

    @Column(name = "promotion_date", nullable = false)
    private Date promotionDate;
}
