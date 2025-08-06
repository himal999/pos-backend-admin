package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CashBookResponseDTO {
    private List<SalesResponseDTO> sales;
    private List<SalesReturnsResponseDTO> returns;
    private List<SalesInOutResponseDTO> inOuts;
    private BigDecimal totalSales;
    private BigDecimal totalReturns;
    private BigDecimal totalCashIn;
    private BigDecimal totalCashOut;
    private BigDecimal balance;
}
