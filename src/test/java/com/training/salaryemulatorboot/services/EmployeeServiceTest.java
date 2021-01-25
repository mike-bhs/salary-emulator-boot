package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.dto.PromotionDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void Should_CreateEmployee_When_ValidData() {
        Employee employee = Factory.getEmployee();
        Position position = employee.getPosition();
        EmployeeDTO employeeDTO = employeeDTO(employee);

        when(positionRepository.findById(position.getId())).thenReturn(Optional.of(position));
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeDTO actualResult = employeeService.createEmployee(employeeDTO);

        verify(employeeRepository, times(1)).save(employee);
        verify(positionRepository, times(1)).findById(position.getId());
        verify(promotionService, times(1)).createInitialPromotion(employee);

        assertAll("Properties set correctly",
                () -> assertEquals(employee.getId(), actualResult.getId()),
                () -> assertEquals(position.getId(), actualResult.getPositionId()),
                () -> assertEquals(employee.getName(), actualResult.getName()),
                () -> assertEquals(employee.getSalaryCurrency(), actualResult.getSalaryCurrency()),
                () -> assertEquals(0, actualResult.getSalaryAmount().compareTo(employee.getSalaryAmount()))
        );
    }

    @Test
    public void Should_Promote_Employee_Salary() {
        Employee employee = Factory.getEmployee();

        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setNewSalaryAmount(new BigDecimal("1700"));
        promotionDTO.setPromotionDate(new Date());

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setSalaryAmount(promotionDTO.getNewSalaryAmount());

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<EmployeeDTO> employeeDTOOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        EmployeeDTO actualResult = employeeDTOOptional.get();

        verify(promotionService, times(1))
                .createSalaryPromotion(employee, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertAll("Properties set correctly",
                () -> assertEquals(updatedEmployee.getId(), actualResult.getId()),
                () -> assertEquals(updatedEmployee.getPosition().getId(), actualResult.getPositionId()),
                () -> assertEquals(updatedEmployee.getName(), actualResult.getName()),
                () -> assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency()),
                () -> assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()))
        );
    }

    @Test
    public void Should_Promote_Employee_Position() {
        Employee employee = Factory.getEmployee();
        Position newPosition = Factory.getPosition(111L, "engineering_manager");

        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setNewPositionId(newPosition.getId());
        promotionDTO.setPromotionDate(new Date());

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setPosition(newPosition);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(newPosition.getId())).thenReturn(Optional.of(newPosition));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<EmployeeDTO> employeeDTOOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        EmployeeDTO actualResult = employeeDTOOptional.get();

        verify(promotionService, times(1))
                .createPositionPromotion(employee, newPosition, promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertAll("Properties set correctly",
                () -> assertEquals(updatedEmployee.getId(), actualResult.getId()),
                () -> assertEquals(newPosition.getId(), actualResult.getPositionId()),
                () -> assertEquals(updatedEmployee.getName(), actualResult.getName()),
                () -> assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency()),
                () -> assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()))
        );
    }

    @Test
    public void Should_Promote_Employee_Salary_And_Position() {
        Employee employee = Factory.getEmployee();
        Position newPosition = Factory.getPosition(111L, "engineering_manager");

        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setNewPositionId(newPosition.getId());
        promotionDTO.setNewSalaryAmount(new BigDecimal("3456"));
        promotionDTO.setPromotionDate(new Date());

        Employee updatedEmployee = Factory.getEmployee();
        updatedEmployee.setPosition(newPosition);
        updatedEmployee.setSalaryAmount(promotionDTO.getNewSalaryAmount());

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(newPosition.getId())).thenReturn(Optional.of(newPosition));
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        Optional<EmployeeDTO> employeeDTOOptional = employeeService.promoteEmployee(employee.getId(), promotionDTO);
        EmployeeDTO actualResult = employeeDTOOptional.get();

        verify(promotionService, times(1))
                .createPromotion(employee, newPosition, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(updatedEmployee);

        assertAll("Properties set correctly",
                () -> assertEquals(updatedEmployee.getId(), actualResult.getId()),
                () -> assertEquals(newPosition.getId(), actualResult.getPositionId()),
                () -> assertEquals(updatedEmployee.getName(), actualResult.getName()),
                () -> assertEquals(updatedEmployee.getSalaryCurrency(), actualResult.getSalaryCurrency()),
                () -> assertEquals(0, actualResult.getSalaryAmount().compareTo(updatedEmployee.getSalaryAmount()))
        );
    }

    @TestFactory
    private EmployeeDTO employeeDTO(Employee emp) {
        return EmployeeDTO.builder()
                .id(emp.getId())
                .positionId(emp.getPosition().getId())
                .name(emp.getName())
                .salaryAmount(emp.getSalaryAmount())
                .salaryCurrency(emp.getSalaryCurrency())
                .build();
    }
}
