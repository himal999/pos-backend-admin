package com.dtech.admin.dto.request;

import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.validator.Conditional;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Conditional(selected = "message",
        values = {
                "CUSTOMER_BILLING_FILTER_LIST",
                "CUSTOMER_SETTLE_FILTER_LIST"
        }, required = {"id"}, message = "Id is required")
public class PaginationRequest<T> extends ChannelRequestValidatorDTO {
    @Min(value = 0, message = "Page size must be zero or a positive number")
    private Integer page = 0;
    @Min(value = 10, message = "Page size must be ten or a positive number")
    private Integer size = 10;
    private String sortColumn = "lastModifiedDate";
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private T search;
    private Long id;
}