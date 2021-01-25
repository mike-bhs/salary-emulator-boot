package com.training.salaryemulatorboot.dto;

import com.training.salaryemulatorboot.domain.PromotionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PromotionDTO implements Serializable {
    private Long newPositionId;
    private BigDecimal newSalaryAmount;
    private Date promotionDate;

    public PromotionType getPromotionType() {
        if (newPositionId != null && newSalaryAmount != null) {
            return PromotionType.POSITION_AND_SALARY;
        } else if (newPositionId != null) {
            return PromotionType.POSITION;
        } else if (newSalaryAmount != null) {
            return PromotionType.SALARY;
        } else {
            return PromotionType.NOT_ALLOWED;
        }
    }
}
