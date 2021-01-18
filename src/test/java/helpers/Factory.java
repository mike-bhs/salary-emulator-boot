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

        p.setId("13b7ec7d-c03a-460f-96a1-62de158d9536");
        p.setOldPosition(getPosition());
        p.setNewPosition(getPosition("c9e7ab72-325d-483d-b7a9-8dde69d1fa8d", "senior_software_engineer"));
        p.setEmployee(getEmployee());
        p.setOldSalaryAmount(new BigDecimal("2343.25"));
        p.setNewSalaryAmount(new BigDecimal("3000"));
        p.setPromotionDate(new Date());

        return p;
    }

    @TestFactory
    public static Employee getEmployee() {
        Employee emp = new Employee();

        emp.setId("13b7ec7d-c03a-460f-96a1-62de158d9536");
        emp.setName("Bob");
        emp.setSalaryAmount(new BigDecimal("2343.25"));
        emp.setSalaryCurrency("USD");
        emp.setPosition(getPosition());

        return emp;
    }

    public static Position getPosition() {
        return getPosition("20a881e6-cf77-468c-ac34-30686a33ece8", "software_engineer");
    }

    public static Position getPosition(String id, String name) {
        return getPosition(id, name, null);
    }

    public static Position getPosition(String id, String name, Position manager) {
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setManagerPosition(manager);

        return p;
    }
}
