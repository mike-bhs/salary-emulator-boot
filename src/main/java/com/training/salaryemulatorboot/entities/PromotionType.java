package com.training.salaryemulatorboot.entities;

public enum PromotionType {
    SALARY("salary"),
    POSITION("position"),
    POSITION_AND_SALARY("position_and_salary");

    public final String name;

    PromotionType(String name) {
        this.name = name;
    }
}
