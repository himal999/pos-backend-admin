package com.dtech.admin.dto.request.validator;

import com.dtech.admin.validator.OnAdd;
import com.dtech.admin.validator.OnDelete;
import com.dtech.admin.validator.OnGet;
import com.dtech.admin.validator.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
public class TransferRequestValidatorDTO   {
    @NotNull(message = "ID is required", groups = {OnAdd.class, OnGet.class, OnDelete.class})
    private Long id;
    @NotNull(message = "Qty is required", groups = {OnAdd.class})
    @Min(value = 1, message = "Qty must be at least 1", groups = {OnAdd.class})
    private BigDecimal qty;
}
