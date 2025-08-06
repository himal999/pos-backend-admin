package com.dtech.admin.dto.response;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockItemBaseResponseDTO  extends CommonResponseDTO{
    private String code;
    private String description;
    private String category;
    private String brand;
    private String unitDescription;
    private BigDecimal lablePrice = BigDecimal.ZERO;
    private BigDecimal itemCost  = BigDecimal.ZERO;
    private BigDecimal retailPrice  = BigDecimal.ZERO;
    private BigDecimal wholesalePrice  = BigDecimal.ZERO;
    private Integer retailDiscount = 0;
    private Integer wholesaleDiscount = 0;
    private BigDecimal qty = BigDecimal.valueOf(0);

    public StockItemBaseResponseDTO(String code, String description, String category, String brand, String unitDescription, BigDecimal lablePrice, BigDecimal itemCost, BigDecimal retailPrice, BigDecimal wholesalePrice, Integer retailDiscount, Integer wholesaleDiscount, BigDecimal qty) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.unitDescription = unitDescription;
        this.lablePrice = lablePrice;
        this.itemCost = itemCost;
        this.retailPrice = retailPrice;
        this.wholesalePrice = wholesalePrice;
        this.retailDiscount = retailDiscount;
        this.wholesaleDiscount = wholesaleDiscount;
        this.qty = qty;
    }



}
