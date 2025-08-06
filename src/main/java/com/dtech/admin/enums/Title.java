package com.dtech.admin.enums;

public enum Title implements DescribableEnum {
    MR("Mr"),
    MRS("Mrs"),
    MISS("Miss");

    private final String description;

    Title(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}