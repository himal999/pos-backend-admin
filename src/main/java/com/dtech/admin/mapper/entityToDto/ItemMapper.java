package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.ItemResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Unit;
import com.dtech.admin.model.Item;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class ItemMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ItemResponseDTO mapItemMapper(Item item){
        try {
            log.info("mapBrandMapper mapper {} ", item);
            ItemResponseDTO itemResponseDTO = modelMapper.map(item, ItemResponseDTO.class);
            itemResponseDTO.setStatusDescription(Status.valueOf(itemResponseDTO.getStatus()).getDescription());
            itemResponseDTO.setUnitDescription(Unit.valueOf(itemResponseDTO.getUnit()).getDescription());
            return itemResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
