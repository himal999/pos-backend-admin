package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Transfer;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.TransferMapper;
import com.dtech.admin.model.ItemTransfer;
import com.dtech.admin.repository.ItemTransferRepository;
import com.dtech.admin.repository.LocationRepository;
import com.dtech.admin.service.TransferService;
import com.dtech.admin.specifications.TransferSpecification;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.PaginationUtil;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransferServiceImpl implements TransferService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final ItemTransferRepository itemTransferRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {

            log.info("Transfer page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.TRNP.name());

            List<SimpleBaseDTO> locations = locationRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(st.getCode(), st.getDescription())).toList();

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("locations", locations);
            responseMap.put("defaultStatus", defaultStatus);

            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.TRNP.name()}, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<TransferSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Transfer filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<ItemTransfer> itemTransfers = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemTransferRepository.findAll(TransferSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    itemTransferRepository.findAll(TransferSpecification.getSpecification(), pageable);
            log.info("Transfer filter records {}", itemTransfers);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemTransferRepository.count(TransferSpecification.getSpecification(paginationRequest.getSearch())) :
                    itemTransferRepository.count(TransferSpecification.getSpecification());
            log.info("Transfer filter records map start");
            List<TransferResponseDTO> responseDTOList = itemTransfers.stream()
                    .map(TransferMapper::mapTransferMapper).toList();
            log.info("Transfer filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<TransferResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.TRANSFER_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(TransferRequestDTO transferRequestDTO, Locale locale) {
        try {
            log.info("Transfer view {}", transferRequestDTO);
            return itemTransferRepository.findByIdAndStatusNot(transferRequestDTO.getId(), Status.DELETE).map(it -> {
                TransferResponseDTO transferResponseDTO = TransferMapper.mapTransferMapper(it);
                return ResponseEntity.ok().body(responseUtil.success((Object) transferResponseDTO, messageSource.getMessage(ResponseMessageUtil.TRANSFER_FOUND_BY_ID, new Object[]{transferResponseDTO.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Item transfer not found {}", transferRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1035, messageSource.getMessage(ResponseMessageUtil.TRANSFER_NOT_FOUND_BY_ID, new Object[]{transferRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> transferCancel(TransferRequestDTO transferRequestDTO, Locale locale) {
        try {
            log.info("Transfer delete {}", transferRequestDTO);

            return itemTransferRepository.findByIdAndStatusNot(transferRequestDTO.getId(), Status.DELETE).map(it -> {

                if (it.getTransferStatus().equals(Transfer.RECEIVED)) {
                    log.info("Transfer to location {}", transferRequestDTO.getId());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1036, messageSource.getMessage(ResponseMessageUtil.TRANSFER_ACCEPTED_FROM_TO_LOCATION, null, locale)));
                }

                it.setStatus(Status.DELETE);
                itemTransferRepository.saveAndFlush(it);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.TRANSFER_CANCEL_SUCCESS, null, locale)));
            }).orElseGet(() -> {
                log.info("Item transfer not found {}", transferRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1035, messageSource.getMessage(ResponseMessageUtil.TRANSFER_NOT_FOUND_BY_ID, new Object[]{transferRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

}
