package com.dtech.admin.dto.request;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationRequestDTO extends ChannelRequestDTO {
    private Long id;
    private String code;
    private String description;
    private String status;
    private String city;
    private String contactNumber;
    private String locationType;
}
