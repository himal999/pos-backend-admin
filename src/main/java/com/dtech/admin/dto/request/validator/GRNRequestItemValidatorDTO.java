package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.Status;
import com.dtech.admin.validator.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigDecimal;

@Data
public class GRNRequestItemValidatorDTO {
    @NotNull(message = "ID is required", groups = {OnUpdate.class,OnGRNItemDisable.class})
    private Long id;
    @NotBlank(message = "Item code is required",groups = {OnGet.class,OnAdd.class})
    private String itemCode;
    @NotNull(message = "Label price is required", groups = {OnAdd.class,OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Label price must be greater than 0", groups = {OnAdd.class,OnUpdate.class})
    private BigDecimal lablePrice;
    @NotNull(message = "Item cost is required", groups = {OnAdd.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Item cost must be greater than 0", groups = {OnAdd.class})
    private BigDecimal itemCost;
    @NotNull(message = "Retail price is required", groups = {OnAdd.class,OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Retail price must be greater than 0", groups = {OnAdd.class,OnUpdate.class})
    private BigDecimal retailPrice;
    @NotNull(message = "Wholesale price is required", groups = {OnAdd.class,OnUpdate.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Wholesale price must be greater than 0", groups = {OnAdd.class,OnUpdate.class})
    private BigDecimal wholesalePrice;
    private Integer retailDiscount;
    private Integer wholesaleDiscount;
    @NotNull(message = "Qty is required", groups = {OnAdd.class,OnUpdate.class})
    @DecimalMin(value = "0.00", message = "Qty must be at least 0.00", groups = {OnAdd.class})
    private BigDecimal qty;
    @NotBlank(message = "Status is required", groups = {OnGRNItemDisable.class,OnAdd.class,OnUpdate.class})
    @ValidEnum(message = "Invalid status",enumClass = Status.class,groups = {OnGRNItemDisable.class,OnAdd.class,OnUpdate.class})
    private String status;
}
