package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GRNItemHistoryDTO {
    private Long id;
    private SimpleBaseDTO item;
    private BigDecimal lablePrice;
    private BigDecimal itemCost;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    private BigDecimal qty;
}
