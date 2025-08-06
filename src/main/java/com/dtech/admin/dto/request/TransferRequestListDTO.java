package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransferRequestListDTO extends ChannelRequestDTO {
    private String fromLocation;
    private String toLocation;
    private List<TransferRequestDTO> transferItemList;
    private String senderRemark;
}
