package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    @Mock
    private PositionService positionService;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Factory.getEmployee();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void Should_Return_All_Employees() {
        List<Employee> expectedResult = Arrays.asList(employee);

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<Employee> actualResult = employeeService.getAllEmployees();

        verify(employeeRepository, times(1)).findAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void Should_Return_Employee_By_Id() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Optional<Employee> employeeOptional = employeeService.findById(employee.getId());

        verify(employeeRepository, times(1)).findById(employee.getId());

        assertTrue(employeeOptional.isPresent());
        assertEquals(employee, employeeOptional.get());
    }

    @Test
    public void Should_Return_Empty_Optional_When_Id_Is_Null() {
        Optional<Employee> employeeOptional = employeeService.findById(null);
        assertTrue(employeeOptional.isEmpty());
    }

    @Test
    public void Should_CreateEmployee_When_ValidData() {
        Employee unsavedEmployee = new Employee();
        BeanUtils.copyProperties(employee, unsavedEmployee, "id");

        Position position = employee.getPosition();
        Employee manager = employee.getManager();

        EmployeeDto employeeDto = Factory.getEmployeeDto();

        when(positionService.findById(position.getId())).thenReturn(Optional.of(position));
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.save(unsavedEmployee)).thenReturn(employee);

        Employee actualResult = employeeService.createEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(unsavedEmployee);
        verify(positionService, times(1)).findById(position.getId());
        verify(employeeRepository, times(1)).findById(manager.getId());

        assertEquals(employee.getId(), actualResult.getId());
        assertEquals(position, actualResult.getPosition());
        assertEquals(manager, actualResult.getManager());
        assertEquals(employee.getName(), actualResult.getName());
        assertEquals(employee.getSalaryCurrency(), actualResult.getSalaryCurrency());
        assertEquals(0, actualResult.getSalaryAmount().compareTo(employee.getSalaryAmount()));
    }

    @Test
    public void Should_Update_All_Employee_Fields_Except_Id() {

    }
}
