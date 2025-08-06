package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.TransferRequestListDTO;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.dtoToEntity.ItemTransferMapper;
import com.dtech.admin.mapper.entityToDto.StockMapper;
import com.dtech.admin.model.*;
import com.dtech.admin.repository.ItemTransferDetailsRepository;
import com.dtech.admin.repository.ItemTransferRepository;
import com.dtech.admin.repository.LocationRepository;
import com.dtech.admin.repository.StockRepository;
import com.dtech.admin.service.ItemTransferService;
import com.dtech.admin.specifications.ItemTransferSpecification;
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

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemTransferServiceImpl implements ItemTransferService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final StockRepository stockRepository;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final ItemTransferRepository itemTransferRepository;

    @Autowired
    private final ItemTransferDetailsRepository itemTransferDetailsRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Item Transfer page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.TRMP.name());

            List<StockItemBaseResponseDTO> stockItemBaseResponseDTOS = stockRepository.
                    findAllByStatus(Status.DELETE).stream().map(st -> new StockItemBaseResponseDTO(
                            st.getItem().getCode(),
                            st.getItem().getDescription(),
                            st.getItem().getCategory().getDescription(),
                            st.getItem().getBrand().getDescription(),
                            st.getItem().getUnit().getDescription(),
                            st.getLablePrice(),
                            st.getItemCost(),
                            st.getRetailPrice(),
                            st.getWholesalePrice(),
                            st.getRetailDiscount(),
                            st.getWholesaleDiscount(),
                            st.getQty()
                    )).toList();

            List<SimpleBaseDTO> locations = locationRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(st.getCode(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("items", stockItemBaseResponseDTOS);
            responseMap.put("locations", locations);

            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.TRMP.name()}, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<TransferSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Item transfer filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Stock> stocks = Objects.nonNull(paginationRequest.getSearch()) ?
                    stockRepository.findAll(ItemTransferSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    stockRepository.findAll(ItemTransferSpecification.getSpecification(), pageable);
            log.info("Item transfer filter records {}", stocks);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    stockRepository.count(ItemTransferSpecification.getSpecification(paginationRequest.getSearch())) :
                    stockRepository.count(ItemTransferSpecification.getSpecification());
            log.info("Item transfer filter records map start");
            List<StockResponseDTO> responseDTOList = stocks.stream()
                    .map(StockMapper::mapStockMapper).toList();
            log.info("Item filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<StockResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.STOCK_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> transfer(TransferRequestListDTO transferRequestListDTO, Locale locale) {
        try {
            log.info("Item transfer request list: {}", transferRequestListDTO);

            List<ItemTransferDetails> transferEntities = new ArrayList<>();

            Optional<Location> fromLocation = locationRepository.findByCodeAndStatus(transferRequestListDTO.getFromLocation(), Status.ACTIVE);
            if (fromLocation.isEmpty()) {
                return ResponseEntity.ok().body(responseUtil.error(null, 1034,
                        messageSource.getMessage(ResponseMessageUtil.FROM_LOCATION_UNAVAILABLE,
                                new Object[]{transferRequestListDTO.getFromLocation()}, locale)));
            }

            Optional<Location> toLocation = locationRepository.findByCodeAndStatus(transferRequestListDTO.getToLocation(), Status.ACTIVE);
            if (toLocation.isEmpty()) {
                return ResponseEntity.ok().body(responseUtil.error(null, 1035,
                        messageSource.getMessage(ResponseMessageUtil.TO_LOCATION_UNAVAILABLE,
                                new Object[]{transferRequestListDTO.getToLocation()}, locale)));
            }

            if(fromLocation.get().getCode().equals(toLocation.get().getCode())) {
                log.info("Item transfer request list contains duplicate location");
                return ResponseEntity.ok().body(responseUtil.error(null, 1037,
                        messageSource.getMessage(ResponseMessageUtil.FROM_LOCATION_TO_LOCATION_SAME, null, locale)));
            }

            for (TransferRequestDTO transferRequestDTO : transferRequestListDTO.getTransferItemList()) {

                Optional<Stock> stockOptional = stockRepository.findByIdAndStatus(transferRequestDTO.getId(), Status.ACTIVE);
                if (stockOptional.isEmpty()) {
                    return ResponseEntity.ok().body(responseUtil.error(null, 1032,
                            messageSource.getMessage(ResponseMessageUtil.STOCK_NOT_FOUND_BY_ID,
                                    new Object[]{transferRequestDTO.getId()}, locale)));
                }

                Stock stock = stockOptional.get();

                if (stock.getQty().compareTo(transferRequestDTO.getQty()) < 0) {
                    return ResponseEntity.ok().body(responseUtil.error(null, 1033,
                            messageSource.getMessage(ResponseMessageUtil.OUT_OF_STOCK,
                                    new Object[]{transferRequestDTO.getId()}, locale)));
                }

                calculateItemCost(stock, transferRequestDTO);

                ItemTransferDetails itemTransferDetails = ItemTransferMapper.mapItemTransferDetails(transferRequestDTO, stock);
                transferEntities.add(itemTransferDetails);

                updateToLocationStock(stock.getItem(), toLocation.get(),stock,transferRequestDTO.getQty());
                stock.setQty(stock.getQty().subtract(transferRequestDTO.getQty()));
                stockRepository.saveAndFlush(stock);

            }

            ItemTransfer itemTransfer = ItemTransferMapper.mapItemTransfer(transferRequestListDTO, fromLocation.get(), toLocation.get());

            ItemTransfer savedItemTransfer = itemTransferRepository.saveAndFlush(itemTransfer);
            transferEntities.forEach(val -> val.setItemTransfer(savedItemTransfer));
            itemTransferDetailsRepository.saveAllAndFlush(transferEntities);

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            null,
                            messageSource.getMessage(
                                    ResponseMessageUtil.ITEM_TRANSFER_SUCCESSFULLY,
                                    null,
                                    locale
                            )
                    )
            );

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private void updateToLocationStock(Item item, Location location, Stock tranStock, BigDecimal trnQty) {
        try {

            log.info("Item transfer check item {} {} {}", item.getCode(),tranStock.getId(),trnQty);
            List<Stock> existingItems = stockRepository.findAllByItemAndLocationAndStatus(item, location,Status.ACTIVE);

            Optional<Stock> matchingStock = existingItems.stream()
                    .filter(st -> Objects.equals(st.getLablePrice(), tranStock.getLablePrice()) &&
                            Objects.equals(st.getItemCost(), tranStock.getItemCost()) &&
                            Objects.equals(st.getRetailPrice(), tranStock.getRetailPrice()) &&
                            Objects.equals(st.getWholesalePrice(), tranStock.getWholesalePrice()) &&
                            Objects.equals(st.getRetailDiscount(), tranStock.getRetailDiscount()) &&
                            Objects.equals(st.getWholesaleDiscount(), tranStock.getWholesaleDiscount()))
                    .findFirst();

            if (matchingStock.isPresent()) {
                Stock stockToUpdate = matchingStock.get();
                stockToUpdate.setQty(matchingStock.get().getQty().add(trnQty));
                stockRepository.saveAndFlush(stockToUpdate);
            } else {
                log.info("No matching stock found for update");
                Stock stockToUpdate = new Stock();
                stockToUpdate.setLablePrice(tranStock.getLablePrice());
                stockToUpdate.setItem(item);
                stockToUpdate.setStatus(Status.ACTIVE);
                stockToUpdate.setQty(trnQty);
                stockToUpdate.setLocation(location);
                stockToUpdate.setItemCost(tranStock.getItemCost());
                stockToUpdate.setRetailPrice(tranStock.getRetailPrice());
                stockToUpdate.setWholesalePrice(tranStock.getWholesalePrice());
                stockToUpdate.setRetailDiscount(tranStock.getRetailDiscount());
                stockToUpdate.setWholesaleDiscount(tranStock.getWholesaleDiscount());
                stockRepository.saveAndFlush(stockToUpdate);

            }

        } catch (Exception e) {
            log.error("Error updating stock: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void calculateItemCost(Stock stock, TransferRequestDTO transferRequestDTO) {
        try {
            BigDecimal totalCost = stock.getItemCost()
                    .multiply(transferRequestDTO.getQty());
            transferRequestDTO.setTotCost(totalCost);
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
