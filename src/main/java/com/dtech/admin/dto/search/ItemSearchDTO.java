package com.dtech.admin.dto.search;

import lombok.Data;

@Data
public class ItemSearchDTO {
    private String code;
    private String description;
    private String status;
    private String category;
    private String brand;
    private String unit;
}
