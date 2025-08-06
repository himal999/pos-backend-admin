package com.dtech.admin.dto.search;

import lombok.Data;

@Data
public class SupplierSearchDTO {
    private String firstName;
    private String lastName;
    private String primaryMobile;
    private String primaryEmail;
    private String company;
    private String status;
}
