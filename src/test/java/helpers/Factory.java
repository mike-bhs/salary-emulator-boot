package helpers;

import com.training.salaryemulatorboot.dto.EmployeeDto;
import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;

public class Factory {
    @TestFactory
    public static Employee getEmployee() {
        Employee emp = new Employee();

        emp.setId(101L);
        emp.setName("Bob");
        emp.setSalaryAmount(new BigDecimal("2343.25"));
        emp.setSalaryCurrency("USD");
        emp.setPosition(getPosition());
        emp.setManager(getManagerEmployee());

        return emp;
    }

    @TestFactory
    public static Employee getManagerEmployee() {
        Employee emp = new Employee();

        emp.setId(111L);
        emp.setName("Josh");
        emp.setSalaryAmount(new BigDecimal("2750.5"));
        emp.setSalaryCurrency("USD");
        emp.setPosition(getPosition(115L, "project_manager"));

        return emp;
    }

    @TestFactory
    public static EmployeeDto getEmployeeDto() {
        return EmployeeDto.builder()
                .id(101L)
                .name("Bob")
                .managerId(111L)
                .positionId(123L)
                .salaryAmount(new BigDecimal("2343.25"))
                .salaryCurrency("USD")
                .build();
    }

    @TestFactory
    public static Position getPosition() {
        return getPosition(123L, "software_engineer");
    }

    @TestFactory
    public static Position getPosition(Long id, String name) {
        return getPosition(id, name, null);
    }

    @TestFactory
    public static Position getPosition(Long id, String name, Position manager) {
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setManagerPosition(manager);

        return p;
    }
}
