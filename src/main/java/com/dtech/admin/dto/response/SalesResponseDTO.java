package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesResponseDTO {
    private Long id;
    private String invoiceNumber;
    private String paymentType;
    private String paymentTypeDescription;
    private CustomerResponseDTO customer;
    private String salesType;
    private String salesTypeDescription;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private String remark;
    private List<SalesDetailsResponseDTO> salesDetailsResponseDTOList;
}
