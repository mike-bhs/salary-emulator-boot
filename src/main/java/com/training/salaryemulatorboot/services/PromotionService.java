package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import com.training.salaryemulatorboot.repositories.PromotionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void createPromotion(Employee emp, Position newPosition, BigDecimal newSalary, Date promotionDate) {
        Promotion promotion = buildPromotion(emp, newSalary, promotionDate);

        promotion.setOldSalaryAmount(emp.getSalaryAmount());
        promotion.setOldPosition(emp.getPosition());
        promotion.setNewPosition(newPosition);

        promotionRepository.save(promotion);
    }

    public void createInitialPromotion(Employee emp) {
        Promotion promotion = buildPromotion(emp, emp.getSalaryAmount(), new Date());

        promotion.setNewPosition(emp.getPosition());

        promotionRepository.save(promotion);
    }

    public void createSalaryPromotion(Employee emp, BigDecimal newSalary, Date promotionDate) {
        Promotion promotion = buildPromotion(emp, newSalary, promotionDate);

        promotion.setOldSalaryAmount(emp.getSalaryAmount());
        promotion.setOldPosition(emp.getPosition());
        promotion.setNewPosition(emp.getPosition());

        promotionRepository.save(promotion);
    }

    public void createPositionPromotion(Employee emp, Position newPosition, Date promotionDate) {
        Promotion promotion = buildPromotion(emp, emp.getSalaryAmount(), promotionDate);

        promotion.setOldSalaryAmount(emp.getSalaryAmount());
        promotion.setOldPosition(emp.getPosition());
        promotion.setNewPosition(newPosition);

        promotionRepository.save(promotion);
    }

    private Promotion buildPromotion(Employee emp, BigDecimal newSalary, Date promotionDate) {
        Promotion promotion = new Promotion();

        promotion.setEmployee(emp);
        promotion.setNewSalaryAmount(newSalary);
        promotion.setPromotionDate(promotionDate);

        return promotion;
    }
}
