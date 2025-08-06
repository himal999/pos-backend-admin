package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.StockResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Unit;
import com.dtech.admin.model.Stock;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class StockMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static StockResponseDTO mapStockMapper(Stock stock){
        try {
            log.info("mapStockMapper mapper");
            StockResponseDTO stockResponseDTO = modelMapper.map(stock, StockResponseDTO.class);
            stockResponseDTO.setStatusDescription(Status.valueOf(stockResponseDTO.getStatus()).getDescription());
            stockResponseDTO.getItem().setStatusDescription(Status.valueOf(stockResponseDTO.getItem().getStatus()).getDescription());
            stockResponseDTO.getItem().setUnitDescription(Unit.valueOf(stock.getItem().getUnit().name()).getDescription());
            return stockResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
