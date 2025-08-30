package com.dtech.admin.service.impl;

import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.repository.CashierUserRepository;
import com.dtech.admin.service.ProfitService;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final CashierUserRepository cashierUserRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Profit page reference data {} ", channelRequestDTO);
            Map<String,Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.PROF.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> locations = Arrays.stream(LocationType.values())
                    .map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> cashiers = cashierUserRepository.findAllByStatusNot(Status.DELETE)
                    .stream().map(cashierUser -> new SimpleBaseDTO(String.valueOf(cashierUser.getId()), cashierUser.getUsername()))
                    .toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("locations", locations);
            responseMap.put("cashiers", cashiers);
            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.PROF.name()}, locale)));

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
