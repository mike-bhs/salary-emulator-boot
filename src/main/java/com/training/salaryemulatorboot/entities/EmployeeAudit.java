package com.training.salaryemulatorboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "employees_audit")
public class EmployeeAudit {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "position"})
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "position"})
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "position"})
    private Employee manager;

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    private String name;

    @Column(name = "salary_amount", nullable = false)
    @Min(value = 0, message = "salary_amount should be more than 0")
    private BigDecimal salaryAmount;

    @Column(name = "salary_currency", nullable = false)
    private String salaryCurrency;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
}
