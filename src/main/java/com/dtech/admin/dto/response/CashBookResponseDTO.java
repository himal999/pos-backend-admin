package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CashBookResponseDTO {
    private List<SalesResponseDTO> sales;
    private List<SalesResponseDTO> creditSales;
    private List<SalesReturnsResponseDTO> returns;
    private List<SalesInOutResponseDTO> inOuts;
    private List<SalesInOutResponseDTO> openingClosed;
    private BigDecimal totalSales;
    private BigDecimal totalCreditSales;
    private BigDecimal totalReturns;
    private BigDecimal totalCashIn;
    private BigDecimal totalCashOut;
    private BigDecimal balance;
    private SalesInOutResponseDTO openingBalance;
    private SalesInOutResponseDTO cashierBalance;
    private BigDecimal balanceAmount;
}
