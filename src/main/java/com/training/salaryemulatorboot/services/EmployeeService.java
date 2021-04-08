package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.repositories.EmployeeRepository;
import com.training.salaryemulatorboot.utils.NullAwareBinUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;

    @SneakyThrows
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        if (id != null) {
            return employeeRepository.findById(id);
        } else {
            return Optional.empty();
        }
    }

    public Employee createEmployee(EmployeeDto empDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(empDTO, employee, "id");

        // TODO check if optional isPresent it's required
        Optional<Position> positionOptional = positionService.findById(empDTO.getPositionId());
        Optional<Employee> managerOptional = findById(empDTO.getManagerId());

        positionOptional.ifPresent(employee::setPosition);
        managerOptional.ifPresent(employee::setManager);

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Optional<Employee> employeeOptional = findById(employeeId);
        // TODO check if optional isPresent
        Employee employee = employeeOptional.get();

        NullAwareBinUtils.copyPropertiesIgnoreNull(employeeDto, employee);

        Optional<Position> positionOptional = positionService.findById(employeeDto.getPositionId());
        Optional<Employee> managerOptional = findById(employeeDto.getManagerId());

        positionOptional.ifPresent(employee::setPosition);
        managerOptional.ifPresent(employee::setManager);

        return employeeRepository.save(employee);
    }
}
