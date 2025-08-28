package com.dtech.admin.dto.request;

import com.dtech.admin.dto.search.CustomerSearchDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerRequestDTO extends PaginationRequest<CustomerSearchDTO>{
    private Long id;
    private String invoiceNumber;
}
