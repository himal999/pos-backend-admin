package com.dtech.admin.enums;

public enum Transfer  implements DescribableEnum {
    SENDING("Sending"),
    RECEIVED("Received"),;

    private final String description;

    Transfer(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
