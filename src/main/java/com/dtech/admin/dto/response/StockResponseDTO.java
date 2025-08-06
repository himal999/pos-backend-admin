package com.dtech.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockResponseDTO extends CommonResponseDTO{
    private Long id;
    private ItemResponseDTO item;
    private BigDecimal lablePrice;
    private BigDecimal itemCost;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    private BigDecimal qty;
    private LocationResponseDTO location;
    private String status;
    private String statusDescription;
}
