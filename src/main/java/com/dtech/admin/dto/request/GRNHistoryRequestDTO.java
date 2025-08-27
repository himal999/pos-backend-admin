package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GRNHistoryRequestDTO extends ChannelRequestDTO{
    private Long id;
    private Long supplierId;
}
