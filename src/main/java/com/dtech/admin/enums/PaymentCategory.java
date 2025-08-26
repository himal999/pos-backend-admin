package com.dtech.admin.enums;

public enum PaymentCategory implements DescribableEnum{
    CASH("Cash"),
    CREDIT("Credit");

    private final String description;

    PaymentCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
