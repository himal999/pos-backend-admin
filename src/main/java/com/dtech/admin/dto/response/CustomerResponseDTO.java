package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerResponseDTO {
    private String title;
    private String titleDescription;
    private String firstName;
    private String lastName;
    private String city;
    private String mobile;
    private String email;
    private BigDecimal creditLimit;
    private String status;
    private String statusDescription;
    private BigDecimal pendingBalance;

}
