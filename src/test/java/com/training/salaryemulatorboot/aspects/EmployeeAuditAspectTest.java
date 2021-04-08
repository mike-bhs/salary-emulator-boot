package com.training.salaryemulatorboot.aspects;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.EmployeeAudit;
import com.training.salaryemulatorboot.repositories.EmployeeAuditRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeAuditAspectTest {
    @Mock
    private EmployeeAuditRepository employeeAuditRepository;

    @InjectMocks
    private EmployeeAuditAspect employeeAuditAspect;

    private Employee employee;
    private EmployeeAudit employeeAudit;

    @BeforeEach
    public void setUp() {
        employee = Factory.getEmployee();

        employeeAudit = new EmployeeAudit();
        BeanUtils.copyProperties(employee, employeeAudit, "createdAt", "id");
        employeeAudit.setEmployee(employee);
        
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void Should_Create_Audit_Record() {
        when(employeeAuditRepository.save(employeeAudit)).thenReturn(employeeAudit);

        assertEquals(employee, employeeAuditAspect.createAudit(employee));

        verify(employeeAuditRepository, times(1)).save(employeeAudit);
    }
}
