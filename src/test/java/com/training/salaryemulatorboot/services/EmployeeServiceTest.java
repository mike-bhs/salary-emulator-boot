package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.entities.PromotionType;
import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.dto.PromotionDto;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    @Mock
    private PositionRepository positionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private PromotionDto promotionDTO;

    @BeforeEach
    public void setUp() {
        employee = Factory.getEmployee();
        MockitoAnnotations.initMocks(this);
        promotionDTO = new PromotionDto();
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

        Employee actualResult = employeeService.findById(employee.getId()).get();

        verify(employeeRepository, times(1)).findById(employee.getId());

        assertEquals(employee, actualResult);
    }

    @Test
    public void Should_CreateEmployee_When_ValidData() {
        Employee unsavedEmployee = new Employee();
        BeanUtils.copyProperties(employee, unsavedEmployee, "id");

        Position position = employee.getPosition();
        Employee manager = employee.getManager();

        EmployeeDto employeeDto = Factory.getEmployeeDto();

        when(positionRepository.findById(position.getId())).thenReturn(Optional.of(position));
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.save(unsavedEmployee)).thenReturn(employee);

        Employee actualResult = employeeService.createEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(unsavedEmployee);
        verify(positionRepository, times(1)).findById(position.getId());
        verify(employeeRepository, times(1)).findById(manager.getId());
        verify(promotionService, times(1)).createInitialPromotion(unsavedEmployee);

        assertEquals(employee.getId(), actualResult.getId());
        assertEquals(position, actualResult.getPosition());
        assertEquals(manager, actualResult.getManager());
        assertEquals(employee.getName(), actualResult.getName());
        assertEquals(employee.getSalaryCurrency(), actualResult.getSalaryCurrency());
        assertEquals(0, actualResult.getSalaryAmount().compareTo(employee.getSalaryAmount()));
    }

    @Test
    public void Should_Promote_Employee_Salary() {
        promotionDTO.setNewSalaryAmount(new BigDecimal("1700"));
        promotionDTO.setPromotionDate(new Date());
        promotionDTO.setPromotionType(PromotionType.SALARY);

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setSalaryAmount(promotionDTO.getNewSalaryAmount());

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<Employee> employeeOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        Employee actualResult = employeeOptional.get();

        verify(promotionService, times(1))
                .createSalaryPromotion(employee, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertEquals(updatedEmployee.getId(), actualResult.getId());
        assertEquals(updatedEmployee.getPosition(), actualResult.getPosition());
        assertEquals(updatedEmployee.getName(), actualResult.getName());
        assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency());
        assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()));
    }

    @Test
    public void Should_Promote_Employee_Position() {
        Position newPosition = Factory.getPosition(111L, "engineering_manager");

        promotionDTO.setNewPositionId(newPosition.getId());
        promotionDTO.setPromotionDate(new Date());
        promotionDTO.setPromotionType(PromotionType.POSITION);

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setPosition(newPosition);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(newPosition.getId())).thenReturn(Optional.of(newPosition));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<Employee> employeeOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        Employee actualResult = employeeOptional.get();

        verify(promotionService, times(1))
                .createPositionPromotion(employee, newPosition, promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertEquals(updatedEmployee.getId(), actualResult.getId());
        assertEquals(newPosition, actualResult.getPosition());
        assertEquals(updatedEmployee.getName(), actualResult.getName());
        assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency());
        assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()));
    }

    @Test
    public void Should_Promote_Employee_Position_And_Salary() {
        Position newPosition = Factory.getPosition(111L, "engineering_manager");

        promotionDTO.setNewPositionId(newPosition.getId());
        promotionDTO.setNewSalaryAmount(new BigDecimal("3456"));
        promotionDTO.setPromotionDate(new Date());
        promotionDTO.setPromotionType(PromotionType.POSITION_AND_SALARY);

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setPosition(newPosition);
        updatedEmployee.setSalaryAmount(promotionDTO.getNewSalaryAmount());

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(newPosition.getId())).thenReturn(Optional.of(newPosition));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<Employee> employeeOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        Employee actualResult = employeeOptional.get();

        verify(promotionService, times(1))
                .createPromotion(employee, newPosition, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertEquals(updatedEmployee.getId(), actualResult.getId());
        assertEquals(newPosition, actualResult.getPosition());
        assertEquals(updatedEmployee.getName(), actualResult.getName());
        assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency());
        assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()));
    }
}
