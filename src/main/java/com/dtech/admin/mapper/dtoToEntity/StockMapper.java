package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.GRNRequestItemDTO;
import com.dtech.admin.dto.request.StockRequestDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Item;
import com.dtech.admin.model.Location;
import com.dtech.admin.model.Stock;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StockMapper {
    public static void mapStock(StockRequestDTO dto, Item item, Location location, Stock stock) {
        log.info("mapStock");
        stock.setItem(item);
        stock.setLocation(location);
        stock.setLablePrice(dto.getLablePrice());
        stock.setRetailPrice(dto.getRetailPrice());
        stock.setWholesalePrice(dto.getWholesalePrice());
        stock.setRetailDiscount(dto.getRetailDiscount());
        stock.setWholesaleDiscount(dto.getWholesaleDiscount());
        stock.setQty(dto.getQty());
        stock.setStatus(Status.valueOf(dto.getStatus()));
    }
}
