package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import com.training.salaryemulatorboot.dto.EmployeeDTO;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.repositories.PositionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    public EmployeeService(EmployeeRepository empRepo, PositionRepository positionRepo) {
        this.employeeRepository = empRepo;
        this.positionRepository = positionRepo;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> findById(String id) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        return employeeData.map(this::convertToDTO);
    }

    public EmployeeDTO createEmployee(EmployeeDTO empDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(empDTO, employee);
        employee.setId(UUID.randomUUID().toString());

        Optional<Position> positionOptional = positionRepository.findById(empDTO.getPositionId());
        positionOptional.ifPresent(employee::setPosition);

        employee = employeeRepository.save(employee);

        return convertToDTO(employee);
    }

    public Employee promoteEmployee(Employee employee, Promotion promotion) {
        employee.setPosition(promotion.getNewPosition());
        employee.setSalaryAmount(promotion.getNewSalaryAmount());

        return employeeRepository.save(employee);
    }

    private EmployeeDTO convertToDTO(Employee emp) {
        EmployeeDTO empDTO = new EmployeeDTO();
        BeanUtils.copyProperties(emp, empDTO);
        empDTO.setPositionId(emp.getPosition().getId());

        return empDTO;
    }
}
