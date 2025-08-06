package com.dtech.admin.dto.search;

import lombok.Data;

import java.util.Date;

@Data
public class TransferSearchDTO {
    private String code;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String fromDate;
    private String toDate;
    private String transferStatus;
    private String status;
}
