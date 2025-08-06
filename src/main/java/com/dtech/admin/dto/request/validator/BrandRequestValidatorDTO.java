package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.Status;
import com.dtech.admin.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrandRequestValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "ID is required",groups = {OnGet.class, OnUpdate.class, OnDelete.class})
    private Long id;
    @NotBlank(message = "Code is required",groups = {OnAdd.class, OnUpdate.class})
    private String code;
    @NotBlank(message = "Description is required",groups = {OnAdd.class, OnUpdate.class})
    private String description;
    @NotBlank(message = "Status is required",groups = {OnAdd.class, OnUpdate.class})
    @ValidEnum(message = "Invalid status",enumClass = Status.class,groups = {OnAdd.class,OnUpdate.class})
    private String status;
}
