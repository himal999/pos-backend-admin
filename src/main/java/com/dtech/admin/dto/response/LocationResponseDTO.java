package com.dtech.admin.dto.response;

import com.dtech.admin.dto.CommonResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationResponseDTO extends CommonResponseDTO {
    private String city;
    private String contactNumber;
    private String locationType;
    private String locationTypeDescription;
}
