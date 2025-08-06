package com.dtech.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierResponseDTO extends CommonResponseDTO{
    private Long id;
    private String title;
    private String titleDescription;
    private String status;
    private String statusDescription;
    private String firstName;
    private String lastName;
    private String primaryMobile;
    private String primaryEmail;
    private String company;
}
