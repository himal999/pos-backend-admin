package com.dtech.admin.dto.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesReturnsDetailsResponseDTO {
    private Long id;
    private BigDecimal qty;
    private String itemCode;
    private String itemName;
}
