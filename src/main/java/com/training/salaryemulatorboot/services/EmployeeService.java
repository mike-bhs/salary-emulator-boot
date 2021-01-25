package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.dto.PromotionDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final PromotionService promotionService;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    public EmployeeService(EmployeeRepository empRepo, PositionRepository positionRepo, PromotionService promotionservice) {
        this.promotionService = promotionservice;
        this.employeeRepository = empRepo;
        this.positionRepository = positionRepo;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> findById(Long id) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        return employeeData.map(this::convertToDTO);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO empDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(empDTO, employee);

        Optional<Position> positionOptional = positionRepository.findById(empDTO.getPositionId());
        positionOptional.ifPresent(employee::setPosition);
        promotionService.createInitialPromotion(employee);

        employee = employeeRepository.save(employee);

        return convertToDTO(employee);
    }

    @Transactional
    public Optional<EmployeeDTO> promoteEmployee(Long employeeId, PromotionDTO promotionDTO) {
        return employeeRepository.findById(employeeId).map(employee -> {
            employee = doPromotion(employee, promotionDTO);
            return Optional.of(convertToDTO(employee));
        }).orElse(Optional.empty());
    }

    private Employee doPromotion(Employee employee, PromotionDTO promotionDTO) {
        switch (promotionDTO.getPromotionType()) {
            case SALARY:
                return raiseSalary(employee, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate());

            case POSITION:
                return positionRepository.findById(promotionDTO.getNewPositionId())
                        .map(position -> raisePosition(employee, position, promotionDTO.getPromotionDate()))
                        .orElse(employee);

            case POSITION_AND_SALARY:
                return positionRepository.findById(promotionDTO.getNewPositionId())
                        .map(position -> raisePositionAndSalary(employee, position, promotionDTO.getNewSalaryAmount(), promotionDTO.getPromotionDate()))
                        .orElse(employee);

            default:
                return employee;
        }
    }

    private Employee raisePositionAndSalary(Employee employee, Position newPosition, BigDecimal newSalaryAmount, Date promotionDate) {
        promotionService.createPromotion(employee, newPosition, newSalaryAmount, promotionDate);

        employee.setPosition(newPosition);
        employee.setSalaryAmount(newSalaryAmount);

        return employeeRepository.save(employee);
    }

    private Employee raisePosition(Employee employee, Position newPosition, Date promotionDate) {
        promotionService.createPositionPromotion(employee, newPosition, promotionDate);

        employee.setPosition(newPosition);

        return employeeRepository.save(employee);
    }

    private Employee raiseSalary(Employee employee, BigDecimal newSalaryAmount, Date promotionDate) {
        promotionService.createSalaryPromotion(employee, newSalaryAmount, promotionDate);

        employee.setSalaryAmount(newSalaryAmount);

        return employeeRepository.save(employee);
    }

    private EmployeeDTO convertToDTO(Employee emp) {
        return EmployeeDTO.builder()
                .id(emp.getId())
                .positionId(emp.getPosition().getId())
                .name(emp.getName())
                .salaryAmount(emp.getSalaryAmount())
                .salaryCurrency(emp.getSalaryCurrency())
                .build();
    }
}
