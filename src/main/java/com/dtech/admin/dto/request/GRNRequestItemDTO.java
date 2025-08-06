package com.dtech.admin.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigDecimal;

@Data
public class GRNRequestItemDTO {
    private Long id;
    private String itemCode;
    private BigDecimal lablePrice;
    private BigDecimal itemCost;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    private BigDecimal qty;
    private String status;
}
