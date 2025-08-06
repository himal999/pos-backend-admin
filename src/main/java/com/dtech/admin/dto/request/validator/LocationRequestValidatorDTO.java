package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationRequestValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "ID is required",groups = {OnGet.class, OnUpdate.class, OnDelete.class})
    private Long id;
    @NotBlank(message = "Code is required",groups = {OnAdd.class, OnUpdate.class})
    private String code;
    @NotBlank(message = "Description is required",groups = {OnAdd.class, OnUpdate.class})
    private String description;
    @NotBlank(message = "City is required",groups = {OnAdd.class, OnUpdate.class})
    private String city;
    @NotBlank(message = "Contact number is required",groups = {OnAdd.class, OnUpdate.class})
    @Pattern(regexp = "^(071|070|074|077|075|078|072|076)[0-9]{7}$", message = "Invalid mobile number. It must start with 071,074, 070, 077, 075, 078, 072, or 076, and be followed by 7 digits",groups = {OnAdd.class, OnUpdate.class})
    private String contactNumber;
    @NotBlank(message = "Status is required",groups = {OnAdd.class, OnUpdate.class})
    @ValidEnum(message = "Invalid status",enumClass = Status.class,groups = {OnAdd.class,OnUpdate.class})
    private String status;
    @NotBlank(message = "Location type is required",groups = {OnAdd.class, OnUpdate.class})
    @ValidEnum(message = "Invalid location type",enumClass = LocationType.class,groups = {OnAdd.class,OnUpdate.class})
    private String locationType;
}
