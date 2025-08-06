package com.dtech.admin.dto.search;

import lombok.Data;

@Data
public class StockSearchDTO {
    private String location;
    private String itemCode;
    private String itemDescription;
    private String qty;
    private String qtyOperator;
}