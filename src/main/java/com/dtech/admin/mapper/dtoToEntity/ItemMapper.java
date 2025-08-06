package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.model.Brand;
import com.dtech.admin.model.Category;
import com.dtech.admin.model.Item;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class ItemMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Item mapItem(ItemRequestDTO itemRequestDTO, Category category, Brand brand) {
        try {
            log.info("Item mapper start dto to entity");
            Item item = modelMapper.map(itemRequestDTO, Item.class);
            item.setBrand(brand);
            item.setCategory(category);
            log.info("Item  mapper  dto to entity {} ", item);
            return item;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
