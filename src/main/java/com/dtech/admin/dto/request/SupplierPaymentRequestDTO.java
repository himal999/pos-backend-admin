package com.dtech.admin.dto.request;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper= true)
@Data
public class SupplierPaymentRequestDTO extends ChannelRequestDTO{
    private Long supplierId;
    private Long grnId;
    private BigDecimal paymentAmount;
}
