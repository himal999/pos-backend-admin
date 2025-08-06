package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemGRNResponseDTO extends CommonResponseDTO{
    private Long id;
    private ItemResponseDTO item;
    private BigDecimal lablePrice = BigDecimal.ZERO;
    private BigDecimal itemCost  = BigDecimal.ZERO;
    private BigDecimal retailPrice  = BigDecimal.ZERO;
    private BigDecimal wholesalePrice  = BigDecimal.ZERO;
    private Integer retailDiscount = 0;
    private Integer wholesaleDiscount = 0;
    private BigDecimal qty = BigDecimal.valueOf(0);
    private GRNResponseDTO grn;
    private String status;
    private String statusDescription;
    private SimpleBaseDTO location;
}
