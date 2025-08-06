package com.dtech.admin.dto.search;

import lombok.Data;

@Data
public class LocationSearchDTO {
    private String code;
    private String description;
    private String status;
    private String city;
    private String contactNumber;
    private String locationType;
}
