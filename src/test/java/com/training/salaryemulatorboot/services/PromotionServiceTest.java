package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.entities.Employee;
import com.training.salaryemulatorboot.entities.Position;
import com.training.salaryemulatorboot.entities.Promotion;
import com.training.salaryemulatorboot.repositories.PromotionRepository;
import helpers.Factory;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.*;

public class PromotionServiceTest {
    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private PromotionService promotionService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        employee = Factory.getEmployee();
    }

    @Test
    public void Should_Create_Initial_Promotion() {
        Employee employee = Factory.getEmployee();
        Promotion initialPromotion = getInitialPromotion(employee);

        promotionService.createInitialPromotion(employee);

        verify(promotionRepository, times(1)).save(initialPromotion);
    }

    @Test
    public void Should_Create_Salary_Promotion() {
        BigDecimal newSalary = new BigDecimal("123.45");
        Date promotionDate = new Date();
        Promotion salaryPromotion = getSalaryPromotion(employee, newSalary, promotionDate);

        promotionService.createSalaryPromotion(employee, newSalary, promotionDate);

        verify(promotionRepository, times(1)).save(salaryPromotion);
    }

    @Test
    public void Should_Create_Position_Promotion() {
        Position newPosition = Factory.getPosition(111L, "engineering_manager");
        Date promotionDate = new Date();
        Promotion positionPromotion = getPositionPromotion(employee, newPosition, promotionDate);

        promotionService.createPositionPromotion(employee, newPosition, promotionDate);

        verify(promotionRepository, times(1)).save(positionPromotion);
    }

    @Test
    public void Should_Create_Position_And_Salary_Promotion() {
        BigDecimal newSalary = new BigDecimal("123.45");
        Position newPosition = Factory.getPosition(111L, "engineering_manager");
        Date promotionDate = new Date();
        Promotion promotion = getPositionPromotion(employee, newPosition, promotionDate);
        promotion.setNewSalaryAmount(newSalary);

        promotionService.createPromotion(employee, newPosition, newSalary, promotionDate);

        verify(promotionRepository, times(1)).save(promotion);
    }

    @TestFactory
    private Promotion getInitialPromotion(Employee employee) {
        Promotion promotion = new Promotion();

        promotion.setEmployee(employee);
        promotion.setNewSalaryAmount(employee.getSalaryAmount());
        promotion.setPromotionDate(new Date());
        promotion.setNewPosition(employee.getPosition());

        return promotion;
    }

    @TestFactory
    private Promotion getSalaryPromotion(Employee employee, BigDecimal newSalary, Date promotionDate) {
        Promotion promotion = new Promotion();

        promotion.setEmployee(employee);
        promotion.setOldSalaryAmount(employee.getSalaryAmount());
        promotion.setNewSalaryAmount(newSalary);
        promotion.setPromotionDate(promotionDate);
        promotion.setOldPosition(employee.getPosition());
        promotion.setNewPosition(employee.getPosition());

        return promotion;
    }

    @TestFactory
    private Promotion getPositionPromotion(Employee employee, Position newPosition, Date promotionDate) {
        Promotion promotion = new Promotion();

        promotion.setEmployee(employee);
        promotion.setOldSalaryAmount(employee.getSalaryAmount());
        promotion.setNewSalaryAmount(employee.getSalaryAmount());
        promotion.setPromotionDate(promotionDate);
        promotion.setOldPosition(employee.getPosition());
        promotion.setNewPosition(newPosition);

        return promotion;
    }
}