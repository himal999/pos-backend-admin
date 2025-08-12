package com.dtech.admin.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SupplierBalanceResponseDTO {
    private Long supplierId;
    private String supplierName;
    private List<GRNBalanceDTO> grnBalances;
}

