package com.training.salaryemulatorboot.repositories;

import com.training.salaryemulatorboot.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Transactional
    Optional<Employee> findBySalaryAmount(BigDecimal salaryAmount);
}
