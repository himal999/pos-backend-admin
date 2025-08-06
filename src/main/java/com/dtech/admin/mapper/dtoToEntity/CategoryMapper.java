package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.CategoryRequestDTO;
import com.dtech.admin.model.Category;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class CategoryMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Category mapCategory(CategoryRequestDTO categoryRequestDTO) {
        try {
            log.info("Category mapper start dto to entity");
            Category category = modelMapper.map(categoryRequestDTO, Category.class);
            log.info("Category user role mapper  dto to entity {} ", category);
            return category;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
