package com.dtech.admin.dto.request.validator;

import com.dtech.admin.validator.OnAdd;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class TransferRequestListValidatorDTO extends ChannelRequestValidatorDTO {
    @NotNull(message = "From location is required", groups = {OnAdd.class})
    private String fromLocation;
    @NotNull(message = "To location is required", groups = {OnAdd.class})
    private String toLocation;
    @NotNull(message = "Transfer item(s) required",groups = {OnAdd.class})
    @Valid
    @NotEmpty(message = "Transfer item(s) required", groups = {OnAdd.class})
    private List<TransferRequestValidatorDTO> transferItemList;
    private String senderRemark;
}
