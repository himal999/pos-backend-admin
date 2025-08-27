package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.GRNHistoryResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.GRNHistory;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class GRNHistoryMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static GRNHistoryResponseDTO mapHistoryMapper(GRNHistory grnHistory){
        try {
            log.info("mapHistoryMapper mapper {} ", grnHistory);
            GRNHistoryResponseDTO grnHistoryResponseDTO = modelMapper.map(grnHistory, GRNHistoryResponseDTO.class);
            grnHistoryResponseDTO.setStatusDescription(Status.valueOf(grnHistoryResponseDTO.getStatus()).getDescription());
            return grnHistoryResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
