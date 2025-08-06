package com.dtech.admin.enums;

public enum Facility implements DescribableEnum{
    INSURANCE("Insurance"),
    DEATH("Death"),
    BOTH("Both");

    private final String description;

    Facility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
