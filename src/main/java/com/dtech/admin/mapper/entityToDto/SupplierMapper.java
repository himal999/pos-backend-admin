package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.SupplierResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.model.Supplier;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class SupplierMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static SupplierResponseDTO mapSupplierMapper(Supplier supplier){
        try {
            log.info("mapCategoryMapper mapper {} ", supplier);
            SupplierResponseDTO supplierResponseDTO = modelMapper.map(supplier, SupplierResponseDTO.class);
            supplierResponseDTO.setStatusDescription(Status.valueOf(supplierResponseDTO.getStatus()).getDescription());
            supplierResponseDTO.setTitleDescription(Title.valueOf(supplierResponseDTO.getTitle()).getDescription());
            return supplierResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
