package com.training.salaryemulatorboot.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 4775241577737129044L;
    private Long id;
    private String name;
    private Long positionId;
    private BigDecimal salaryAmount;
    private String salaryCurrency;
}
