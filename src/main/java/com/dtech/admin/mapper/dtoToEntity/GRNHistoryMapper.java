package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.GRNRequestDTO;
import com.dtech.admin.dto.request.GRNRequestItemDTO;
import com.dtech.admin.model.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;


@Log4j2
public class GRNHistoryMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static GRNHistory mapGrnHistory(GRNRequestDTO grnRequestDTO, Supplier supplier, Location location) {
        try {
            log.info("GRN mapper start dto to entity");

            TypeMap<GRNRequestDTO, GRNHistory> typeMap = modelMapper.typeMap(GRNRequestDTO.class, GRNHistory.class);
            typeMap.addMappings(mapper -> mapper.skip(GRNHistory::setItemGRNS));
            GRNHistory grn = modelMapper.map(grnRequestDTO, GRNHistory.class);
            grn.setId(null);
            grn.setSupplier(supplier);
            grn.setLocation(location);
            log.info("GRN  mapper  dto to entity {} ", grn);
            return grn;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public static GRNItemHistory mapItemGrnHistory(GRNRequestItemDTO grnRequestItemDTO, Item item,GRNHistory grn) {
        try {
            log.info("GRN item mapper start dto to entity");
            GRNItemHistory itemGRN = modelMapper.map(grnRequestItemDTO, GRNItemHistory.class);
            itemGRN.setItem(item);
            itemGRN.setGrnHistory(grn);
            log.info("GRN item mapper  dto to entity {} ", itemGRN);
            return itemGRN;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
