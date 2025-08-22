package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.CashInOut;
import com.dtech.admin.enums.CashierBalanceRequestType;
import com.dtech.admin.validator.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class CashierBalanceActionRequestValidatorDTO extends ChannelRequestValidatorDTO{
    @NotNull(message = "Id is required",groups = {OnDelete.class, OnUpdate.class})
    private Long id;
    @NotNull(message = "Amount is required",groups = {OnAdd.class,OnUpdate.class})
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than 0",groups = {OnAdd.class,OnUpdate.class})
    private BigDecimal amount;
    @NotBlank(message = "Cash In/Out type is required",groups = {OnAdd.class})
    @ValidEnum(enumClass = CashInOut.class,message = "Invalid cash In/Out type",groups = {OnAdd.class})
    private String cashInOut;
    @NotNull(message = "Cash In/Out date is required",groups = {OnAdd.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date cashInOutDate;
    @NotBlank(message = "Cashier user is required",groups = {OnAdd.class})
    private String cashierUser;
    @NotBlank(message = "Remark is required",groups = {OnAdd.class,OnUpdate.class})
    private String remark;
    @NotBlank(message = "Request type is required",groups = {OnDelete.class})
    @ValidEnum(enumClass = CashierBalanceRequestType.class,message = "Invalid request type type",groups = {OnDelete.class})
    private String requestType;
}
