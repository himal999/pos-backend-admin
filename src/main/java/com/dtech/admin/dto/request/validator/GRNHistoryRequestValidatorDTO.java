package com.dtech.admin.dto.request.validator;

import com.dtech.admin.validator.OnGet;
import com.dtech.admin.validator.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNHistoryRequestValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "Id is required", groups = {OnGet.class, OnUpdate.class})
    private Long id;
    @NotNull(message = "Supplier id is required",groups = {OnUpdate.class})
    private Long supplierId;
}
