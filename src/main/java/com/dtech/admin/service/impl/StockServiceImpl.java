package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;

import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.StockRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.response.ItemGRNResponseDTO;
import com.dtech.admin.dto.response.StockResponseDTO;
import com.dtech.admin.dto.search.StockSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.GRNItemMapper;
import com.dtech.admin.mapper.entityToDto.StockMapper;
import com.dtech.admin.model.Stock;
import com.dtech.admin.repository.LocationRepository;
import com.dtech.admin.repository.MeasureRepository;
import com.dtech.admin.repository.StockRepository;
import com.dtech.admin.service.StockService;
import com.dtech.admin.specifications.StockSpecification;
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
public class StockServiceImpl implements StockService {

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
    private final StockRepository stockRepository;

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

            responseMap.put("privileges", privileges);
            responseMap.put("locations", locations);
            responseMap.put("measure", measure);
            responseMap.put("defaultStatus", defaultStatus);

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
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<StockSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Stock filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Stock> stocks = Objects.nonNull(paginationRequest.getSearch()) ?
                    stockRepository.findAll(StockSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    stockRepository.findAll(StockSpecification.getSpecification(), pageable);
            log.info("Stock filter records {}", stocks);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    stockRepository.count(StockSpecification.getSpecification(paginationRequest.getSearch())) :
                    stockRepository.count(StockSpecification.getSpecification());
            log.info("Stock filter records map start");
            List<StockResponseDTO> responseDTOList = stocks.stream()
                    .map(StockMapper::mapStockMapper).toList();
            log.info("Stock filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<StockResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.STOCK_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(StockRequestDTO stockRequestDTO, Locale locale) {
        try {
            log.info("Stock view request process {}", stockRequestDTO);

          return stockRepository.findAllByIdAndStatusNot(stockRequestDTO.getId(), Status.DELETE).map(st -> {
                log.info("Start mapper process");
                ItemGRNResponseDTO itemGRNResponseDTOS =
                        GRNItemMapper.mapItemGRNMapper(st);

                return ResponseEntity.ok().body(
                        responseUtil.success((Object)
                                itemGRNResponseDTOS,
                                messageSource.getMessage(
                                        ResponseMessageUtil.STOCK_FOUND_BY_ID,
                                        new Object[]{stockRequestDTO.getId()},
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("Stock item not found {}", stockRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1031, messageSource.getMessage(ResponseMessageUtil.STOCK_NOT_FOUND_BY_ID, new Object[]{stockRequestDTO.getId()}, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(StockRequestDTO stockRequestDTO, Locale locale) {
        try {
            log.info("Stock update request process {}", stockRequestDTO);

            return stockRepository.findById(stockRequestDTO.getId()).map(st -> {

                String newModel = new StringBuilder()
                        .append(stockRequestDTO.getLablePrice())
                        .append("|")
                        .append(stockRequestDTO.getRetailPrice())
                        .append("|")
                        .append(stockRequestDTO.getWholesalePrice())
                        .append("|")
                        .append(stockRequestDTO.getQty()).toString();

                String oldModel = new StringBuilder()
                        .append(st.getLablePrice())
                        .append("|")
                        .append(st.getRetailPrice())
                        .append("|")
                        .append(st.getWholesalePrice())
                        .append("|")
                        .append(st.getQty()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Stock update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1032, messageSource.getMessage(ResponseMessageUtil.STOCK_VALUES_NOT_CHANGING, null, locale)));
                }

                com.dtech.admin.mapper.dtoToEntity.StockMapper.mapStock(stockRequestDTO, st.getItem(), st.getLocation(), st);
                log.info("Stock update status changed to {} ", st.toString());
                stockRepository.saveAndFlush(st);

                return ResponseEntity.ok().body(
                        responseUtil.success(
                                null,
                                messageSource.getMessage(
                                        ResponseMessageUtil.STOCK_UPDATE_SUCCESS,
                                        new Object[]{st.getItem().getCode()},
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("Stock item not found {}", stockRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1031, messageSource.getMessage(ResponseMessageUtil.ITEM_NOT_FOUND_BY_ID, new Object[]{stockRequestDTO.getId()}, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> stockEnableDisable(StockRequestDTO stockRequestDTO, Locale locale) {
        try {
            log.info("Stock inactive request process {}", stockRequestDTO);
            return stockRepository.findById(stockRequestDTO.getId()).map(item -> {

                String newModel = new StringBuilder()
                        .append(stockRequestDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(item.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Stock inactive update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1032, messageSource.getMessage(ResponseMessageUtil.STOCK_VALUES_NOT_CHANGING, null, locale)));
                }

                item.setStatus(Status.valueOf(stockRequestDTO.getStatus()));
                stockRepository.saveAndFlush(item);

                return ResponseEntity.ok().body(
                        responseUtil.success(
                                null,
                                messageSource.getMessage(
                                        ResponseMessageUtil.STOCK_UPDATE_SUCCESS,
                                        new Object[]{item.getItem().getCode()},
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("Stock inactive not found {}", stockRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1031, messageSource.getMessage(ResponseMessageUtil.STOCK_NOT_FOUND_BY_ID, new Object[]{stockRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
