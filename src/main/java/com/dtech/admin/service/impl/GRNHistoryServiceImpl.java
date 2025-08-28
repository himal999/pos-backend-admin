package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;

import com.dtech.admin.dto.request.GRNHistoryRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.dto.search.GRNHistorySearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.GRNHistoryMapper;
import com.dtech.admin.model.GRNHistory;
import com.dtech.admin.repository.*;
import com.dtech.admin.service.GRNHistoryService;
import com.dtech.admin.specifications.GRNHistorySpecification;
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
@Log4j2
@RequiredArgsConstructor
public class GRNHistoryServiceImpl implements GRNHistoryService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final MeasureRepository measureRepository;

    @Autowired
    private final GRNHistoryRepository grnHistoryRepository;

    @Autowired
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Stock page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.STMP.name());

            List<SimpleBaseDTO> locations = locationRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(st.getCode(), st.getDescription())).toList();

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> measure = measureRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(st.getCode(), st.getDescription())).toList();

            List<SimpleBaseDTO> supplierList = supplierRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(String.valueOf(st.getId()), Title.valueOf(st.getTitle().name()) + "." + st.getFirstName()+" "+st.getLastName())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("locations", locations);
            responseMap.put("measure", measure);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("suppliers", supplierList);

            return ResponseEntity.ok().body(responseUtil.success(responseMap,
                    messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS,
                            new Object[]{WebPage.STMP.name()}, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<GRNHistorySearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("GRN history filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<GRNHistory> grnHistory = Objects.nonNull(paginationRequest.getSearch()) ?
                    grnHistoryRepository.findAll(GRNHistorySpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    grnHistoryRepository.findAll(GRNHistorySpecification.getSpecification(), pageable);
            log.info("GRN history filter records {}", grnHistory);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    grnHistoryRepository.count(GRNHistorySpecification.getSpecification(paginationRequest.getSearch())) :
                    grnHistoryRepository.count(GRNHistorySpecification.getSpecification());
            log.info("GRN history filter records map start");
            List<GRNHistoryResponseDTO> responseDTOList = grnHistory.stream()
                    .map(GRNHistoryMapper::mapHistoryMapper).toList();
            log.info("GRN history filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<GRNHistoryResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.GRN_HISTORY_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(GRNHistoryRequestDTO grnHistoryRequestDTO, Locale locale) {
        try {
            log.info("GRN history view request process {}", grnHistoryRequestDTO);

          return grnHistoryRepository.findByIdAndStatusNot(grnHistoryRequestDTO.getId(), Status.DELETE).map(gh -> {
                log.info("Start mapper process");
              GRNHistoryResponseDTO grnHistoryResponseDTO =
                        GRNHistoryMapper.mapHistoryMapper(gh);

                return ResponseEntity.ok().body(
                        responseUtil.success((Object)
                                        grnHistoryResponseDTO,
                                messageSource.getMessage(
                                        ResponseMessageUtil.GRN_HISTORY_FOUND_BY_ID,
                                        new Object[]{grnHistoryRequestDTO.getId()},
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("GRN history not found {}", grnHistoryRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1042, messageSource.getMessage(ResponseMessageUtil.GRN_HISTORY_NOT_FOUND_BY_ID, new Object[]{grnHistoryRequestDTO.getId()}, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(GRNHistoryRequestDTO grnHistoryRequestDTO, Locale locale) {
        try {
            log.info("GRN history update request process {}", grnHistoryRequestDTO);

            return grnHistoryRepository.findByIdAndStatusNot(grnHistoryRequestDTO.getId(),
                    Status.DELETE).map(gh -> {

                    return  supplierRepository.findByIdAndStatusNot(grnHistoryRequestDTO.getSupplierId(), Status.DELETE).map(supplier -> {

                            String newModel = new StringBuilder()
                                    .append(grnHistoryRequestDTO.getSupplierId()).toString();

                            String oldModel = new StringBuilder()
                                    .append(gh.getSupplier().getId()).toString();

                            if (oldModel.equals(newModel)) {
                                log.info("GRN history update status not changed to {}", newModel);
                                return ResponseEntity.ok().body(responseUtil.error(null, 1032, messageSource.getMessage(ResponseMessageUtil.STOCK_VALUES_NOT_CHANGING, null, locale)));
                            }

                            log.info("Stock update status changed to {} ", supplier.getId().toString());
                            gh.setSupplier(supplier);
                            grnHistoryRepository.saveAndFlush(gh);

                            return ResponseEntity.ok().body(
                                    responseUtil.success(
                                            null,
                                            messageSource.getMessage(
                                                    ResponseMessageUtil.GRN_HISTORY_UPDATE_SUCCESS,
                                                    new Object[]{gh.getId()},
                                                    locale
                                            )
                                    )
                            );
                        }).orElseGet(() -> {
                            log.info("Supplier not found {}", grnHistoryRequestDTO.getSupplierId());
                            return ResponseEntity.ok().body(responseUtil.error(null, 1043, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID, new Object[]{grnHistoryRequestDTO.getSupplierId()}, locale)));

                        });

            }).orElseGet(() -> {
                log.info("GRN history not found {}", grnHistoryRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1042, messageSource.getMessage(ResponseMessageUtil.GRN_HISTORY_NOT_FOUND_BY_ID, new Object[]{grnHistoryRequestDTO.getId()}, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
