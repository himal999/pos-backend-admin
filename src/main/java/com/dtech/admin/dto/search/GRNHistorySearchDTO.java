package com.dtech.admin.dto.search;

import lombok.Data;

@Data
public class GRNHistorySearchDTO {
    private String locationCode;
    private String itemCode;
    private String itemDescription;
    private Long supplierId;
    private String qty;
    private String qtyOperator;
    private String status;
}