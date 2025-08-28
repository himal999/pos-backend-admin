package com.dtech.admin.dto.response;


import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillingResponseDTO {
    private Long id;
    private String invoiceNumber;
    private CashierUserResponseDTO cashierUser;
    private String paymentType;
    private String paymentTypeDescription;
    private SimpleBaseDTO location;
    private String salesType;
    private String salesTypeDescription;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private String remark;
    private Date createDate;
    private BigDecimal actualPayAmount;
    private String paymentCategory;
    private String paymentCategoryDescription;
    private BigDecimal pendingBalance;

}
