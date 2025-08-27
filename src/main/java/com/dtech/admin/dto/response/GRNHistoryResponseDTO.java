package com.dtech.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNHistoryResponseDTO extends CommonResponseDTO {
    private Long id;
    private BigDecimal cost;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private SupplierResponseDTO supplier;
    private LocationResponseDTO location;
    private List<GRNItemHistoryDTO> itemGRNS;
    private String remark;
    private Date dueDate;
    private String status;
    private String statusDescription;
}
