package com.dtech.admin.dto.request.validator;


import com.dtech.admin.validator.OnAdd;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNRequestValidatorDTO extends ChannelRequestValidatorDTO{
    @NotNull(message = "Supplier id is required",groups = {OnAdd.class})
    private Long supplierId;
    @NotBlank(message = "Location code is required",groups = {OnAdd.class})
    private String locationCode;
    @NotNull(message = "Cost code is required",groups = {OnAdd.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Cost must be greater than 0", groups = {OnAdd.class})
    private BigDecimal cost;
    @NotNull(message = "Cost code is required",groups = {OnAdd.class})
    @DecimalMin(value = "0.00", inclusive = true, message = "Debit amount must be greater than 0", groups = {OnAdd.class})
    private BigDecimal debitAmount;
    @NotNull(message = "Cost code is required",groups = {OnAdd.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Credit amount must be greater than 0", groups = {OnAdd.class})
    private BigDecimal creditAmount;
    @NotNull(message = "Item(s) code is required",groups = {OnAdd.class})
    @Valid
    private List<GRNRequestItemValidatorDTO> itemGRNS;
    private String remark;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date dueDate;
}
