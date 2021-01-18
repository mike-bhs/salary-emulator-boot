package com.training.salaryemulatorboot.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 4775241577737129044L;
    private String id;
    private String name;
    private String positionId;
    private BigDecimal salaryAmount;
    private String salaryCurrency;
}
