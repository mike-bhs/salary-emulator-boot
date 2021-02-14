package com.training.salaryemulatorboot.dto;

import com.training.salaryemulatorboot.entities.PromotionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PromotionDto implements Serializable {
    private Long newPositionId;
    private BigDecimal newSalaryAmount;
    private Date promotionDate;
    private PromotionType promotionType;
}
