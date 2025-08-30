package com.dtech.admin.dto.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class GRNHistorySearchDTO {
    private String locationCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String toDate;
    private Long supplierId;
    private String qty;
    private String qtyOperator;
    private String status;
}