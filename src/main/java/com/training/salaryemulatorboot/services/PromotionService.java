package com.training.salaryemulatorboot.services;

import com.training.salaryemulatorboot.domain.Employee;
import com.training.salaryemulatorboot.domain.Position;
import com.training.salaryemulatorboot.domain.Promotion;
import com.training.salaryemulatorboot.repositories.PromotionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Promotion createPromotion(Employee emp, Position newPosition, BigDecimal newSalary, Date promotionDate) {
        Promotion promotion = new Promotion();

        promotion.setId(UUID.randomUUID().toString());
        promotion.setEmployeeRelatedFields(emp);
        promotion.setNewSalaryAmount(newSalary);
        promotion.setNewPosition(newPosition);
        promotion.setPromotionDate(promotionDate);

        return promotionRepository.save(promotion);
    }
}
