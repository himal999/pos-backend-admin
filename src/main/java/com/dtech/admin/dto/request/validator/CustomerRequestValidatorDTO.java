package com.dtech.admin.dto.request.validator;

import com.dtech.admin.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerRequestValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "Id is required",groups = {BasicInfo.class,CreditLimit.class, OnDelete.class, CreditCashStatus.class})
    private Long id;
    @NotBlank(message = "Invoice number is required",groups = {OnGet.class})
    private String invoiceNumber;
    @NotBlank(message = "Title is required",groups = {BasicInfo.class})
    private String title;
    @NotBlank(message = "Firstname is required",groups = {BasicInfo.class})
    private String firstName;
    @NotBlank(message = "Lastname is required",groups = {BasicInfo.class})
    private String lastName;
    @NotBlank(message = "City is required",groups = {BasicInfo.class})
    private String city;
    @NotBlank(message = "Mobile number is required",groups = {BasicInfo.class})
    private String mobile;
    private String email;
    @NotNull(message = "Credit limit number is required",groups = {CreditLimit.class})
    private BigDecimal creditLimit;
    @NotNull(message = "Cash credit status is required",groups = {CreditCashStatus.class})
    private Boolean isActiveCredit;

}
