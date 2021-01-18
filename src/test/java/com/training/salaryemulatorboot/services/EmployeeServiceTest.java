package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import helpers.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Why it doesn't work ???
// @ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private PositionRepository positionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Should_CreateEmployee_When_ValidData() {
        Employee employee = Factory.getEmployee();
        Position position = employee.getPosition();
        UUID employeeUuid = UUID.fromString(employee.getId());
        Optional<Position> positionOptional = Optional.of(position);
        EmployeeDTO employeeDTO = employeeDTO(employee);

        try (MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(employeeUuid);

            when(positionRepository.findById(position.getId())).thenReturn(positionOptional);
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeDTO actualResult = employeeService.createEmployee(employeeDTO);

            verify(employeeRepository, times(1)).save(employee);
            verify(positionRepository, times(1)).findById(position.getId());

            assertAll("Properties set correctly",
                    () -> assertEquals(employee.getId(), actualResult.getId()),
                    () -> assertEquals(position.getId(), actualResult.getPositionId()),
                    () -> assertEquals(employee.getName(), actualResult.getName()),
                    () -> assertEquals(employee.getSalaryCurrency(), actualResult.getSalaryCurrency()),
                    () -> assertEquals(0, actualResult.getSalaryAmount().compareTo(employee.getSalaryAmount()))
            );
        }
    }

    @Test
    public void Should_Change_Employee_Position_And_Salary() {
        Promotion promotion = Factory.getPromotion();

        Employee promotedEmployee = Factory.getEmployee();
        promotedEmployee.setPosition(promotion.getNewPosition());
        promotedEmployee.setSalaryAmount(promotion.getNewSalaryAmount());

        when(employeeRepository.save(promotedEmployee)).thenReturn(promotedEmployee);

        Employee employee = employeeService.promoteEmployee(Factory.getEmployee(), promotion);

        verify(employeeRepository, times(1)).save(promotedEmployee);

        assertEquals(promotedEmployee, employee);
    }

    @TestFactory
    private EmployeeDTO employeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setName(employee.getName());
        employeeDTO.setSalaryAmount(employee.getSalaryAmount());
        employeeDTO.setSalaryCurrency(employee.getSalaryCurrency());
        employeeDTO.setPositionId(employee.getPosition().getId());

        return employeeDTO;
    }
}
