package com.dtech.admin.dto.request;

import lombok.Data;


import java.math.BigDecimal;
import java.math.BigDecimal;

@Data
public class TransferRequestDTO {
    private Long id;
    private BigDecimal qty;
    private BigDecimal totCost;
}
