package com.dtech.admin.dto.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class CustomerSearchDTO {
    private String name;
    private String mobile;
    private String city;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String toDate;
    private String paymentCategory;
    private String location;
    private String settlementType;
}
