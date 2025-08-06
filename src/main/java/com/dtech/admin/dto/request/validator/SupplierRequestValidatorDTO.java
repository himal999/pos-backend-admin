package com.dtech.admin.dto.request.validator;

import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierRequestValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "ID is required",groups = {OnGet.class, OnUpdate.class, OnDelete.class})
    private Long id;
    @NotBlank(message = "Title is required",groups = {OnAdd.class, OnUpdate.class})
    @ValidEnum(message = "Invalid title",enumClass = Title.class,groups = {OnAdd.class,OnUpdate.class})
    private String title;
    @NotBlank(message = "First name is required",groups = {OnAdd.class, OnUpdate.class})
    private String firstName;
    @NotBlank(message = "Last name is required",groups = {OnAdd.class, OnUpdate.class})
    private String lastName;
    @NotBlank(message = "Primary mobile is required",groups = {OnAdd.class, OnUpdate.class})
    @Pattern(regexp = "^(071|070|074|077|075|078|072|076)[0-9]{7}$", message = "Invalid mobile number. It must start with 071,074, 070, 077, 075, 078, 072, or 076, and be followed by 7 digits",groups = {OnAdd.class, OnUpdate.class})
    private String primaryMobile;
    @NotBlank(message = "Primary email is required",groups = {OnAdd.class, OnUpdate.class})
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please enter a valid email address",groups = {OnAdd.class, OnUpdate.class})
    private String primaryEmail;
    @NotBlank(message = "Company is required",groups = {OnAdd.class, OnUpdate.class})
    private String company;
    @NotBlank(message = "Status is required",groups = {OnAdd.class, OnUpdate.class})
    @ValidEnum(message = "Invalid status",enumClass = Status.class,groups = {OnAdd.class,OnUpdate.class})
    private String status;
}
