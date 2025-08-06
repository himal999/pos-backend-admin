package com.dtech.admin.dto.request;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierRequestDTO extends ChannelRequestDTO {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String primaryMobile;
    private String primaryEmail;
    private String company;
    private String status;
}
