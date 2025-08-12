package com.dtech.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierPaymentResponseDTO extends CommonResponseDTO {
    private Long id;
    private Long supplierId;
    private Long grnId;
    private BigDecimal paymentAmount;
    private Date paymentDate;
}
