package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesDetailsResponseDTO {
    private Long id;
    private BigDecimal qty;
    private BigDecimal salesPrice;
    private Double salesDiscount;
    private BigDecimal itemCost;
    private BigDecimal lablePrice;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private BigDecimal totalPrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    private ItemResponseDTO item;

}
