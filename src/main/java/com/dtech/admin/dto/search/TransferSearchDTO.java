package com.dtech.admin.dto.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TransferSearchDTO {
    private String code;
    private String description;
    private String fromLocation;
    private String toLocation;
    @JsonFormat(pattern = "yyyy/MM/dd",shape = JsonFormat.Shape.STRING)
    private String fromDate;
    @JsonFormat(pattern = "yyyy/MM/dd",shape = JsonFormat.Shape.STRING)
    private String toDate;
    private String transferStatus;
    private String status;
}
