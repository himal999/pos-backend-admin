package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.LocationResponseDTO;
import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Location;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class LocationMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationResponseDTO mapLocationMapper(Location location){
        try {
            log.info("mapBrandMapper mapper {} ", location);
            LocationResponseDTO locationResponseDTO = modelMapper.map(location, LocationResponseDTO.class);
            locationResponseDTO.setStatusDescription(Status.valueOf(locationResponseDTO.getStatus()).getDescription());
            locationResponseDTO.setLocationTypeDescription(LocationType.valueOf(locationResponseDTO.getLocationType()).getDescription());
            return locationResponseDTO;
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }
}
