package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashierBalanceActionRequestDTO extends ChannelRequestDTO{
    private Long id;
    private String requestType;
    private BigDecimal amount;
    private String remark;
    private String cashInOut;
    private Date cashInOutDate;
    private String cashierUser;
}
