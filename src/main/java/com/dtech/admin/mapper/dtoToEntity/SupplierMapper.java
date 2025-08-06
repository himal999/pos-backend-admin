package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.SupplierRequestDTO;
import com.dtech.admin.model.Supplier;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class SupplierMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Supplier mapSupplier(SupplierRequestDTO supplierRequestDTO) {
        try {
            log.info("Supplier mapper start dto to entity");
            Supplier supplier = modelMapper.map(supplierRequestDTO, Supplier.class);
            log.info("Supplier mapper  dto to entity {} ", supplier);
            return supplier;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
