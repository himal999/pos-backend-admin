package com.dtech.admin.dto.response;

import lombok.Data;

@Data
public class SalesInOutResponseDTO {
    private Long id;
    private String cashInOut;
    private String cashInOutDescription;
    private String remark;
    private Double amount;
}
