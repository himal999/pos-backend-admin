package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigDecimal;

@Data
public class ItemTransferDetailsResponseDTO {
    private Long id;
    private BigDecimal totCost;
    private BigDecimal qty;
    private ItemResponseDTO item;
    private BigDecimal lablePrice;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
}
