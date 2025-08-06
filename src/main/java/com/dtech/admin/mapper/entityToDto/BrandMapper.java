package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.CommonResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Brand;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class BrandMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CommonResponseDTO mapBrandMapper(Brand brand){
        try {
            log.info("mapBrandMapper mapper {} ", brand);
            CommonResponseDTO commonResponseDTO = modelMapper.map(brand, CommonResponseDTO.class);
            commonResponseDTO.setStatusDescription(Status.valueOf(commonResponseDTO.getStatus()).getDescription());
            return commonResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
