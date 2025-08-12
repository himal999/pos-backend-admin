package com.dtech.admin.dto.request.validator;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.validator.OnAdd;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierPaymentRequestValidatorDTO extends ChannelRequestDTO {
    @NotNull(message = "Supplier ID is required", groups = {OnAdd.class})
    private Long supplierId;

    @NotNull(message = "GRN ID is required", groups = {OnAdd.class})
    private Long grnId;

    @NotNull(message = "Payment amount is required", groups = {OnAdd.class})
    @DecimalMin(value = "0.01", inclusive = true, message = "Payment amount must be greater than 0", groups = {OnAdd.class})
    private BigDecimal paymentAmount;
}
