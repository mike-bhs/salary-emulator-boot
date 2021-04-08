package com.training.salaryemulatorboot.aspects;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.EmployeeAudit;
import com.training.salaryemulatorboot.repositories.EmployeeAuditRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class EmployeeAuditAspect {
    private final EmployeeAuditRepository employeeAuditRepository;

    @AfterReturning(pointcut = "execution(* com.training.salaryemulatorboot.services.EmployeeService.updateEmployee(..))"
            + " || execution(* com.training.salaryemulatorboot.services.EmployeeService.createEmployee(..))",
            returning = "result")
    public Object createAudit(Object result) {
        Employee employee = (Employee) result;
        EmployeeAudit employeeAudit = new EmployeeAudit();

        BeanUtils.copyProperties(employee, employeeAudit, "createdAt", "id");
        employeeAudit.setEmployee(employee);

        employeeAuditRepository.save(employeeAudit);

        return result;
    }
}
