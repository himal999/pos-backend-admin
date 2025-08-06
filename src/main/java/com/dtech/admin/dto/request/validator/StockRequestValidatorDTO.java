package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.Status;
import com.dtech.admin.validator.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockRequestValidatorDTO extends ChannelRequestValidatorDTO{
    @NotNull(message = "ID is required", groups = {OnGet.class,OnUpdate.class, OnGRNItemDisable.class})
    private Long id;
    @NotNull(message = "Label price is required", groups = {OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Label price must be greater than 0", groups = {OnUpdate.class})
    private BigDecimal lablePrice;
    @NotNull(message = "Retail price is required", groups = {OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Retail price must be greater than 0", groups = {OnUpdate.class})
    private BigDecimal retailPrice;
    @NotNull(message = "Wholesale price is required", groups = {OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Wholesale price must be greater than 0", groups = {OnUpdate.class})
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    @NotNull(message = "Qty is required", groups = {OnUpdate.class})
    @Min(value = 1, message = "Qty must be at least 1", groups = {OnUpdate.class})
    private BigDecimal qty;
    @NotBlank(message = "Status is required", groups = {OnGRNItemDisable.class,OnUpdate.class})
    @ValidEnum(message = "Invalid status",enumClass = Status.class,groups = {OnGRNItemDisable.class,OnUpdate.class})
    private String status;
}