package com.training.salaryemulatorboot.mappers;

import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "position", target = "positionId", qualifiedByName = "positionToPositionId")
    @Mapping(source = "manager", target = "managerId", qualifiedByName = "managerToManagerId")
    EmployeeDto toEmployeeDto(Employee employee);

    List<EmployeeDto> toEmployeeDtos(List<Employee> employees);

    @Named("positionToPositionId")
    static Long positionToPositionId(Position position) {
        return  position.getId();
    }

    @Named("managerToManagerId")
    static Long managerToManagerId(Employee manager) {
        if (manager != null) {
            return  manager.getId();
        } else {
            return null;
        }
    }
}
