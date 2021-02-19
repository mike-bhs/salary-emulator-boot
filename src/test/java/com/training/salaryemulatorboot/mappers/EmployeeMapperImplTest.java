package com.training.salaryemulatorboot.mappers;

import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.entities.Employee;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperImplTest {
    private Employee employee;
    private EmployeeDto employeeDto;
    private EmployeeMapperImpl employeeMapper;

    @BeforeEach
    public void setUp() {
        employee = Factory.getEmployee();
        employeeDto = Factory.getEmployeeDto();
        employeeMapper = new EmployeeMapperImpl();
    }

    @Test
    public void Should_Map_Employee_To_Dto_When_All_Fields_Present() {
        assertEquals(employeeDto, employeeMapper.toEmployeeDto(employee));
    }

    @Test
    public void Should_Map_Employee_To_Dto_When_Manager_Is_Null() {
        employee.setManager(null);
        employeeDto.setManagerId(null);

        assertEquals(employeeDto, employeeMapper.toEmployeeDto(employee));
    }

    @Test
    public void Should_Map_Employees_To_Dtos() {
        assertEquals(Arrays.asList(employeeDto), employeeMapper.toEmployeeDtos(Arrays.asList(employee)));
    }
}
