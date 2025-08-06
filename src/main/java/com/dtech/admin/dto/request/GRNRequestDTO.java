package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNRequestDTO extends ChannelRequestDTO{
    private Long supplierId;
    private String locationCode;
    private BigDecimal cost;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private List<GRNRequestItemDTO> itemGRNS;
    private String remark;
    private Date dueDate;
}
