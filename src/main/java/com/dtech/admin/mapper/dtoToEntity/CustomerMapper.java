package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.CustomerRequestDTO;
import com.dtech.admin.model.Customer;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class CustomerMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static void mapCustomerMapper(CustomerRequestDTO customerRequestDTO,Customer customer) {
        try {
            log.info("mapCustomerMapper mapper {} ", customerRequestDTO);
              modelMapper.map(customerRequestDTO,customer);
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

}
