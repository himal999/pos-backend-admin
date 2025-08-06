package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransferResponseDTO extends CommonResponseDTO {
    private Long id;
    private SimpleBaseDTO fromLocation;
    private SimpleBaseDTO toLocation;
    private String senderRemark;
    private String accepts;
    private Date acceptedDate;
    private String receivedRemark;
    private String status;
    private String statusDescription;
    private String transferStatus;
    private String transferStatusDescription;
    private List<ItemTransferDetailsResponseDTO> itemTransferDetails;
}
