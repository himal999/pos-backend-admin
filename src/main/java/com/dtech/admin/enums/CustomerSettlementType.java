package com.dtech.admin.enums;

public enum CustomerSettlementType implements DescribableEnum{
    BILLING("Billing"),
    SETTLEMENT("Settlement");

    private final String description;

    CustomerSettlementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}