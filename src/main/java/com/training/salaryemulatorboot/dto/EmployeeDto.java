package com.training.salaryemulatorboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto implements Serializable {
    private static final long serialVersionUID = 4775241577737129044L;
    private Long id;
    private String name;
    private Long positionId;
    private BigDecimal salaryAmount;
    private String salaryCurrency;
}
