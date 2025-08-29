package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerRequestDTO extends ChannelRequestDTO {
    private Long id;
    private String invoiceNumber;
    private String title;
    private String firstName;
    private String lastName;
    private String city;
    private String mobile;
    private String email;
    private BigDecimal creditLimit;
    private Boolean isActiveCredit;
}
