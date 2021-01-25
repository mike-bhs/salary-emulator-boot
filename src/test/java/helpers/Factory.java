package helpers;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Date;

public class Factory {
    @TestFactory
    public static Promotion getPromotion() {
        Promotion p = new Promotion();

        p.setOldPosition(getPosition());
        p.setNewPosition(getPosition(23L, "senior_software_engineer"));
        p.setEmployee(getEmployee());
        p.setOldSalaryAmount(new BigDecimal("2343.25"));
        p.setNewSalaryAmount(new BigDecimal("3000"));
        p.setPromotionDate(new Date());

        return p;
    }

    @TestFactory
    public static Employee getEmployee() {
        Employee emp = new Employee();

        emp.setId(101L);
        emp.setName("Bob");
        emp.setSalaryAmount(new BigDecimal("2343.25"));
        emp.setSalaryCurrency("USD");
        emp.setPosition(getPosition());

        return emp;
    }

    public static Position getPosition() {
        return getPosition(123L, "software_engineer");
    }

    public static Position getPosition(Long id, String name) {
        return getPosition(id, name, null);
    }

    public static Position getPosition(Long id, String name, Position manager) {
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setManagerPosition(manager);

        return p;
    }
}
