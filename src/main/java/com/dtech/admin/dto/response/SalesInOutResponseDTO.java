package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesInOutResponseDTO {
    private Long id;
    private String cashInOut;
    private String cashInOutDescription;
    private String remark;
    private BigDecimal amount;
}
