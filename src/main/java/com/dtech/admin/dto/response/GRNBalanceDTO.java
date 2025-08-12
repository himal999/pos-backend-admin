package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GRNBalanceDTO {
    private Long grnId;
    private BigDecimal cost;
    private BigDecimal paidAmount;
    private BigDecimal balance;
    private List<SupplierPaymentResponseDTO> paymentHistory;
}
