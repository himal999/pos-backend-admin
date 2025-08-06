package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.CommonResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Category;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
@Log4j2
public class CategoryMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CommonResponseDTO mapCategoryMapper(Category category){
        try {
            log.info("mapCategoryMapper mapper {} ", category);
            CommonResponseDTO commonResponseDTO = modelMapper.map(category, CommonResponseDTO.class);
            commonResponseDTO.setStatusDescription(Status.valueOf(commonResponseDTO.getStatus()).getDescription());
            return commonResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
