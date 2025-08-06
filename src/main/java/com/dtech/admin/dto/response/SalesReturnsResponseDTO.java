package com.dtech.admin.dto.response;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesReturnsResponseDTO {
    private Long id;
    private String invoiceNumber;
    private String remark;
    private BigDecimal debitAmount;
    private CustomerResponseDTO customer;
    private List<SalesReturnsDetailsResponseDTO> returnDetails;
}
