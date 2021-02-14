package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.dto.PromotionDto;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee createEmployee(EmployeeDto empDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(empDTO, employee, "id");

        Optional<Position> positionOptional = positionRepository.findById(empDTO.getPositionId());
        positionOptional.ifPresent(employee::setPosition);
        promotionService.createInitialPromotion(employee);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Optional<Employee> promoteEmployee(Long employeeId, PromotionDto promotionDTO) {
        return employeeRepository.findById(employeeId).map(employee -> {
            employee = doPromotion(employee, promotionDTO);
            return Optional.of(employee);
        }).orElse(Optional.empty());
    }

    private Employee doPromotion(Employee employee, PromotionDto promotionDTO) {
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
}
