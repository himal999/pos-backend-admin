package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.GRNRequestDTO;
import com.dtech.admin.dto.request.GRNRequestItemDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.response.ItemGRNResponseDTO;
import com.dtech.admin.dto.response.ItemResponseDTO;
import com.dtech.admin.dto.search.GRNSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.enums.Unit;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.dtoToEntity.GRNHistoryMapper;
import com.dtech.admin.mapper.dtoToEntity.GRNMapper;
import com.dtech.admin.mapper.entityToDto.GRNItemMapper;
import com.dtech.admin.mapper.entityToDto.ItemMapper;
import com.dtech.admin.model.*;
import com.dtech.admin.repository.*;
import com.dtech.admin.service.GRNService;
import com.dtech.admin.specifications.GRNSpecification;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class GRNServiceImpl implements GRNService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final BrandRepository brandRepository;

    @Autowired
    private final SupplierRepository supplierRepository;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final StockRepository stockRepository;

    @Autowired
    private final GRNHistoryRepository grnHistoryRepository;

    @Autowired
    private final ItemGRNHistoryRepository itemGRNHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("GRN page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.GRNP.name());

            log.info("Start already register item");

            //new item
            log.info("Start new item");
            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> categories = categoryRepository.
                    findAllByStatusNot(Status.DELETE).stream()
                    .map(category -> new SimpleBaseDTO(category.getCode(),
                            category.getDescription())).toList();

            List<SimpleBaseDTO> brands = brandRepository.
                    findAllByStatusNot(Status.DELETE).stream()
                    .map(brand -> new SimpleBaseDTO(brand.getCode(),
                            brand.getDescription())).toList();

            List<SimpleBaseDTO> units = Arrays.stream(Unit.values())
                    .map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();
            log.info("End new item");

            //new supplier
            log.info("Start new supplier");
            List<SimpleBaseDTO> title = Arrays.stream(Title.values()).
                    map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            //load already register supplier
            List<SimpleBaseDTO> supplier = supplierRepository.
                    findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(String.valueOf(st.getId()),
                            st.getTitle().getDescription() + "." + st.getFirstName() + " " + st.getLastName())).toList();

            //load locations
            log.info("Start load location");
            List<SimpleBaseDTO> locations = locationRepository
                    .findAllByStatusNot(Status.DELETE).stream()
                    .map(st -> new SimpleBaseDTO(st.getCode(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            //   responseMap.put("items", itemResponseDTOS);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("categories", categories);
            responseMap.put("brands", brands);
            responseMap.put("units", units);
            responseMap.put("title", title);
            responseMap.put("suppliers", supplier);
            responseMap.put("locations", locations);

            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.GRNP.name()}, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<GRNSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("GRN filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Item> items = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemRepository.findAll(GRNSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    itemRepository.findAll(GRNSpecification.getSpecification(), pageable);
            log.info("GRN filter records {}", items);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemRepository.count(GRNSpecification.getSpecification(paginationRequest.getSearch())) :
                    itemRepository.count(GRNSpecification.getSpecification());
            log.info("GRN filter records map start");
            List<ItemResponseDTO> responseDTOList = items.stream()
                    .map(ItemMapper::mapItemMapper).toList();
            log.info("GRN filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<ItemResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.GRN_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(GRNRequestDTO grnRequestDTO, Locale locale) {
        try {
            log.info("GRN request process one {}", grnRequestDTO);

            return supplierRepository.findByIdAndStatus(grnRequestDTO.getSupplierId(), Status.ACTIVE)
                    .map(supplier -> locationRepository.findByCodeAndStatus(grnRequestDTO.getLocationCode(), Status.ACTIVE).map(location -> {
                        String missingItems = checkItems(grnRequestDTO.getItemGRNS());

                        if (missingItems == null || !missingItems.isEmpty()) {
                            log.info("GRN request process missing {} missing items", grnRequestDTO);
                            return ResponseEntity.ok().body(
                                    responseUtil.error(
                                            null,
                                            1030,
                                            messageSource.getMessage(ResponseMessageUtil.ITEM_CODE_MISSING, new Object[]{missingItems}, locale)
                                    )
                            );
                        }

                        List<GRNRequestItemDTO> grnRequestItemDTOS = checkItemCode(grnRequestDTO.getItemGRNS());

                        GRNHistory grnHistory = GRNHistoryMapper.mapGrnHistory(grnRequestDTO, supplier, location);
                        GRNHistory savedHistory = grnHistoryRepository.save(grnHistory);

                        List<Stock> stocks = new ArrayList<>();
                        List<GRNItemHistory> itemGRNHistory = new ArrayList<>();

                        for (GRNRequestItemDTO grnRequestItemDTO : grnRequestItemDTOS) {
                            log.info("Grn request {} ", grnRequestItemDTO);
                            itemRepository.findByCodeAndStatusNot(grnRequestItemDTO.getItemCode(), Status.DELETE).ifPresent(item -> {
                                log.info("item request {} {}", location.getCode() , item.getCode());
                                List<Stock> existingItemOpt = stockRepository.findMatchingItem(
                                        location.getCode(),
                                        item.getCode(),
                                        grnRequestItemDTO.getLablePrice(),
                                        grnRequestItemDTO.getItemCost(),
                                        grnRequestItemDTO.getRetailPrice(),
                                        grnRequestItemDTO.getWholesalePrice(),
                                        grnRequestItemDTO.getRetailDiscount(),
                                        grnRequestItemDTO.getWholesaleDiscount(),
                                        Status.DELETE
                                );

                                if (!existingItemOpt.isEmpty()) {
                                    Stock existingItem = existingItemOpt.getFirst();
                                    existingItem.setQty(existingItem.getQty().add(grnRequestItemDTO.getQty()));
                                    stocks.add(existingItem);
                                    log.info("Existing ItemGRN updated: {}", existingItem);
                                } else {
                                    Stock stock = new Stock();
                                    GRNMapper.mapItemGrn(grnRequestItemDTO, item, location, stock);
                                    stocks.add(stock);
                                    log.info("New ItemGRN created: {}", stock);
                                }

                                GRNItemHistory grnItemHistory = GRNHistoryMapper.mapItemGrnHistory(grnRequestItemDTO, item, savedHistory);
                                itemGRNHistory.add(grnItemHistory);
                            });
                        }

                        stockRepository.saveAllAndFlush(stocks);
                        itemGRNHistoryRepository.saveAll(itemGRNHistory);

                        log.info("GRN process completed successfully for request: {}", grnRequestDTO);
                        return ResponseEntity.ok().body(
                                responseUtil.success(
                                        null,
                                        messageSource.getMessage(ResponseMessageUtil.GRN_ADDED_SUCCESS, null, locale)
                                )
                        );

                    }).orElseGet(() -> {
                        log.info("GRN location not found {}", grnRequestDTO.getLocationCode());
                        return ResponseEntity.ok().body(responseUtil.error(null, 1029,
                                messageSource.getMessage(ResponseMessageUtil.LOCATION_NOT_FOUND_BY_ID,
                                        new Object[]{grnRequestDTO.getLocationCode()}, locale)));
                    })).orElseGet(() -> {
                        log.info("GRN supplier not found {}", grnRequestDTO.getSupplierId());
                        return ResponseEntity.ok().body(responseUtil.error(null, 1028,
                                messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID,
                                        new Object[]{grnRequestDTO.getSupplierId()}, locale)));
                    });

        } catch (Exception e) {
            log.error("Error while processing GRN request", e);
            throw e;
        }
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(GRNRequestItemDTO grnRequestItemDTO, Locale locale) {
        try {
            log.info("GRN view request process {}", grnRequestItemDTO);

            List<Stock> stocks = stockRepository.findAllByItemCodeAndStatusNotNative(grnRequestItemDTO.getItemCode(), Status.DELETE);

            log.info("Start mapper process");
            List<ItemGRNResponseDTO> itemGRNResponseDTOS = stocks.stream()
                    .map(GRNItemMapper::mapItemGRNMapper).toList();

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            itemGRNResponseDTOS,
                            messageSource.getMessage(
                                    ResponseMessageUtil.ITEM_WISE_GRN_RETRIEVED_SUCCESS,
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

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(GRNRequestItemDTO grnRequestItemDTO, Locale locale) {
        try {
            log.info("GRN update request process {}", grnRequestItemDTO);

            return stockRepository.findById(grnRequestItemDTO.getId()).map(item -> {

                String newModel = new StringBuilder()
                        .append(grnRequestItemDTO.getLablePrice())
                        .append("|")
                        .append(grnRequestItemDTO.getRetailPrice())
                        .append("|")
                        .append(grnRequestItemDTO.getWholesalePrice())
                        .append("|")
                        .append(grnRequestItemDTO.getQty()).toString();

                String oldModel = new StringBuilder()
                        .append(item.getLablePrice())
                        .append("|")
                        .append(item.getRetailPrice())
                        .append("|")
                        .append(item.getWholesalePrice())
                        .append("|")
                        .append(item.getQty()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("GRN update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1032, messageSource.getMessage(ResponseMessageUtil.STOCK_VALUES_NOT_CHANGING, null, locale)));
                }

                GRNMapper.mapItemGrn(grnRequestItemDTO, item.getItem(), item.getLocation(), item);
                log.info("GRN update status changed to {} ", item.toString());
                stockRepository.saveAndFlush(item);

                return ResponseEntity.ok().body(
                        responseUtil.success(
                                null,
                                messageSource.getMessage(
                                        ResponseMessageUtil.STOCK_DETAILS_UPDATE_SUCCESS,
                                        new Object[]{item.getItem().getCode()},
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("GRN item not found {}", grnRequestItemDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1031, messageSource.getMessage(ResponseMessageUtil.STOCK_NOT_FOUND_BY_ID, new Object[]{grnRequestItemDTO.getId()}, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> stockEnableDisable(GRNRequestItemDTO grnRequestItemDTO, Locale locale) {
        try {
            log.info("GRN item inactive request process {}", grnRequestItemDTO);
            return stockRepository.findById(grnRequestItemDTO.getId()).map(item -> {

                String newModel = new StringBuilder()
                        .append(grnRequestItemDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(item.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("GRN update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1032, messageSource.getMessage(ResponseMessageUtil.STOCK_VALUES_NOT_CHANGING, null, locale)));
                }
                item.setStatus(Status.valueOf(grnRequestItemDTO.getStatus()));
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
                log.info("GRN item not found {}", grnRequestItemDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1031, messageSource.getMessage(ResponseMessageUtil.STOCK_NOT_FOUND_BY_ID, new Object[]{grnRequestItemDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private String checkItems(List<GRNRequestItemDTO> itemGRNS) {
        try {
            log.info("GRN check items code");
            List<String> missingItemsList = new ArrayList<>();
            String missingItem = "";
            for (GRNRequestItemDTO item : itemGRNS) {
                Item presentItem = itemRepository
                        .findByCodeAndStatus(item.getItemCode(), Status.ACTIVE).orElse(null);

                if (presentItem == null) {
                    log.info("GRN item not found {}", item.getItemCode());
                    missingItemsList.add(item.getItemCode());
                }
                missingItem = String.join(", ", missingItemsList);
            }

            return missingItem;

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private List<GRNRequestItemDTO> checkItemCode(List<GRNRequestItemDTO> itemGRNS) {
        try {
            log.info("GRN check same items");

            Map<String, GRNRequestItemDTO> uniqueItemsMap = new HashMap<>();

            for (GRNRequestItemDTO item : itemGRNS) {
                String key = generateItemKey(item);

                if (uniqueItemsMap.containsKey(key)) {
                    GRNRequestItemDTO existingItem = uniqueItemsMap.get(key);
                    existingItem.setQty(existingItem.getQty().add(item.getQty()));
                } else {
                    uniqueItemsMap.put(key, item);
                }
            }

            return new ArrayList<>(uniqueItemsMap.values());

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private String generateItemKey(GRNRequestItemDTO item) {
        return item.getItemCode() + "|" +
                item.getLablePrice() + "|" +
                item.getItemCost() + "|" +
                item.getRetailPrice() + "|" +
                item.getWholesalePrice() + "|" +
                item.getRetailDiscount() + "|" +
                item.getWholesaleDiscount();
    }


}
