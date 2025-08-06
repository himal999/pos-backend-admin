package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.ItemGRNResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Stock;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class GRNItemMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ItemGRNResponseDTO mapItemGRNMapper(Stock stock){
        try {
            log.info("mapBrandMapper mapper {} ", stock);
            ItemGRNResponseDTO itemGRNResponseDTO = modelMapper.map(stock, ItemGRNResponseDTO.class);
            itemGRNResponseDTO.setStatusDescription(Status.valueOf(itemGRNResponseDTO.getStatus()).getDescription());
            return itemGRNResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
