package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.TransferResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Transfer;
import com.dtech.admin.model.ItemTransfer;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class TransferMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static TransferResponseDTO mapTransferMapper(ItemTransfer transfer) {
        try {
            log.info("mapTransferMapper mapper");
            TransferResponseDTO transferResponseDTO = modelMapper.map(transfer, TransferResponseDTO.class);
            transferResponseDTO.setStatusDescription(Status.valueOf(transferResponseDTO.getStatus()).getDescription());
            transferResponseDTO.setTransferStatusDescription(Transfer.valueOf(transferResponseDTO.getTransferStatus()).getDescription());
            return transferResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e; 
        }
    }
}
