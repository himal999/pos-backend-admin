package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.LocationRequestDTO;
import com.dtech.admin.model.Location;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class LocationMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Location mapLocation(LocationRequestDTO locationRequestDTO) {
        try {
            log.info("Location mapper start dto to entity");
            Location location = modelMapper.map(locationRequestDTO, Location.class);
            log.info("Location user role mapper  dto to entity {} ", location);
            return location;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
