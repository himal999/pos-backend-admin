package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.CashierType;
import com.dtech.admin.validator.OnGet;
import com.dtech.admin.validator.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashierBalanceRequestValidatorDTO extends ChannelRequestValidatorDTO{
    @NotNull(message = "From date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String fromDate;
    @NotNull(message = "To date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String toDate;
    @NotBlank(message = "Invoice number is required",groups = {OnGet.class})
    private String invoiceNumber;
    @NotBlank(message = "Type is required",groups = {OnGet.class})
    @ValidEnum(message = "Invalid enum type",enumClass = CashierType.class,groups = {OnGet.class})
    private String type;
}
