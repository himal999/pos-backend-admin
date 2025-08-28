package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerPaymentHistoryResponseDTO {
    private Long id;
    private String paymentType;
    private String paymentTypeDescription;
    private String invoiceNumber;
    private SimpleBaseDTO location;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private String remark;
    private CashierUserResponseDTO cashierUser;
    private String settlementType;
    private String settlementTypeDescription;
    private BigDecimal afterPaymentPendingBalance;
}
