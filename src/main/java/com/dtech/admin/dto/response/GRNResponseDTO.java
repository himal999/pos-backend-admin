package com.dtech.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNResponseDTO extends CommonResponseDTO{
    private Long id;
    private BigDecimal cost;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private LocationResponseDTO location;
    private String remark;
    private Date dueDate;
}
