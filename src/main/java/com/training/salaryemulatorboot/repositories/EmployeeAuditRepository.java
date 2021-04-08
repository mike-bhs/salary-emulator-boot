package com.training.salaryemulatorboot.repositories;

import com.training.salaryemulatorboot.entities.EmployeeAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmployeeAuditRepository extends JpaRepository<EmployeeAudit, Long> {
    @Query(value = "SELECT COUNT(DISTINCT a1.employee_id) FROM employees_audit AS a1 JOIN (" +
            "SELECT employee_id, MAX(created_at) AS created_at FROM employees_audit WHERE created_at <= :date GROUP BY employee_id" +
            ") AS a2 ON a1.employee_id = a2.employee_id AND a1.created_at = a2.created_at WHERE manager_id = :managerId",
            nativeQuery = true)
    long countSubordinatesOnDate(@Param("managerId") Long managerId, @Param("date") Date date);
}
