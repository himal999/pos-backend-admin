package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.BrandRequestDTO;
import com.dtech.admin.model.Brand;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class BrandMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Brand mapBrand(BrandRequestDTO brandRequestDTO) {
        try {
            log.info("Brand mapper start dto to entity");
            Brand brand = modelMapper.map(brandRequestDTO, Brand.class);
            log.info("Brand user role mapper  dto to entity {} ", brand);
            return brand;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
