package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.response.ItemResponseDTO;
import com.dtech.admin.dto.search.ItemSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Unit;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.ItemMapper;
import com.dtech.admin.model.Item;
import com.dtech.admin.model.ItemSequence;
import com.dtech.admin.repository.BrandRepository;
import com.dtech.admin.repository.CategoryRepository;
import com.dtech.admin.repository.ItemRepository;
import com.dtech.admin.repository.ItemSequenceRepository;
import com.dtech.admin.service.ItemService;
import com.dtech.admin.specifications.ItemSpecification;
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
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final BrandRepository brandRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final ItemSequenceRepository itemSequenceRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Item page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.ITMP.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> categories = categoryRepository.
                    findAllByStatusNot(Status.DELETE).stream().map(category -> new SimpleBaseDTO(category.getCode(),
                            category.getDescription())).toList();

            List<SimpleBaseDTO> brands = brandRepository.
                    findAllByStatusNot(Status.DELETE).stream().map(brand -> new SimpleBaseDTO(brand.getCode(),
                            brand.getDescription())).toList();

            List<SimpleBaseDTO> units = Arrays.stream(Unit.values())
                    .map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("categories", categories);
            responseMap.put("brands", brands);
            responseMap.put("units", units);

            return ResponseEntity.ok().body(
                    responseUtil.success(responseMap,
                            messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS,
                                    new Object[]{WebPage.ITMP.name()},
                                    locale)));

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<ItemSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Item filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Item> items = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemRepository.findAll(ItemSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    itemRepository.findAll(ItemSpecification.getSpecification(), pageable);
            log.info("Item filter records {}", items);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    itemRepository.count(ItemSpecification.getSpecification(paginationRequest.getSearch())) :
                    itemRepository.count(ItemSpecification.getSpecification());
            log.info("Item filter records map start");
            List<ItemResponseDTO> responseDTOList = items.stream()
                    .map(ItemMapper::mapItemMapper).toList();
            log.info("Item filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<ItemResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.ITEM_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(ItemRequestDTO itemRequestDTO, Locale locale) {
        try {
            log.info("Item add {}", itemRequestDTO);

            boolean exists = itemRepository.existsByCodeAndStatusNot(itemRequestDTO.getCode(), Status.DELETE);

            if (exists) {
                log.info("Item code {} already exists", itemRequestDTO.getCode());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1019,
                                messageSource.getMessage(
                                        ResponseMessageUtil.ITEM_CODE_ALREADY_EXISTS,
                                        new Object[]{itemRequestDTO.getCode()},
                                        locale
                                )
                        )
                );
            }

            return categoryRepository.findByCodeAndStatus(itemRequestDTO.getCategory(), Status.ACTIVE).map(category ->
                    brandRepository.findByCodeAndStatus(itemRequestDTO.getBrand(), Status.ACTIVE).map(brand -> {
                        Item item = com.dtech.admin.mapper.dtoToEntity.ItemMapper.mapItem(itemRequestDTO, category, brand);
                        itemRepository.saveAndFlush(item);
                        log.info("Item added {}", brand);

                        return ResponseEntity.ok().body(
                                responseUtil.success(
                                        null,
                                        messageSource.getMessage(
                                                ResponseMessageUtil.ITEM_ADDED_SUCCESS,
                                                new Object[]{itemRequestDTO.getCode()},
                                                locale
                                        )
                                )
                        );
                    }).orElseGet(() -> {
                        log.info("Brand not found {}", itemRequestDTO.getBrand());
                        return ResponseEntity.ok().body(responseUtil.error(null, 1021, messageSource.getMessage(ResponseMessageUtil.BRAND_CODE_NOT_FOUND, null, locale)));
                    })).orElseGet(() -> {
                log.info("Category not found {}", itemRequestDTO.getCategory());
                return ResponseEntity.ok().body(responseUtil.error(null, 1020, messageSource.getMessage(ResponseMessageUtil.CATEGORY_CODE_NOT_FOUND, null, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(ItemRequestDTO itemRequestDTO, Locale locale) {
        try {
            log.info("Item view {}", itemRequestDTO);
            return itemRepository.findByIdAndStatusNot(itemRequestDTO.getId(), Status.DELETE).map(item -> {
                ItemResponseDTO itemResponseDTO = ItemMapper.mapItemMapper(item);
                return ResponseEntity.ok().body(responseUtil.success((Object) itemResponseDTO, messageSource.getMessage(ResponseMessageUtil.ITEM_FOUND_BY_ID, new Object[]{itemResponseDTO.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Item view not found {}", itemRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1021, messageSource.getMessage(ResponseMessageUtil.ITEM_NOT_FOUND_BY_ID, new Object[]{itemRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(ItemRequestDTO itemRequestDTO, Locale locale) {
        try {
            log.info("Item update {}", itemRequestDTO);
            return itemRepository.findByIdAndStatusNot(itemRequestDTO.getId(), Status.DELETE).map(item -> {

                String newModel = new StringBuilder()
                        .append(itemRequestDTO.getDescription())
                        .append("|")
                        .append(itemRequestDTO.getCategory())
                        .append("|")
                        .append(itemRequestDTO.getBrand())
                        .append("|")
                        .append(itemRequestDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(item.getDescription())
                        .append("|")
                        .append(item.getCategory().getCode())
                        .append("|")
                        .append(item.getBrand().getCode())
                        .append("|")
                        .append(item.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Item update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1022, messageSource.getMessage(ResponseMessageUtil.ITEM_VALUES_NOT_CHANGING, null, locale)));
                }

                return categoryRepository.findByCodeAndStatusNot(itemRequestDTO.getCategory(), Status.DELETE).map(category ->
                        brandRepository.findByCodeAndStatusNot(itemRequestDTO.getBrand(), Status.DELETE).map(brand -> {
                            Item modifiedItem = com.dtech.admin.mapper.dtoToEntity.ItemMapper.mapItem(itemRequestDTO, category, brand);
                            itemRepository.saveAndFlush(modifiedItem);
                            log.info("Item update {}", brand);

                            return ResponseEntity.ok().body(
                                    responseUtil.success(
                                            null,
                                            messageSource.getMessage(ResponseMessageUtil.ITEM_UPDATE_SUCCESS,
                                                    new Object[]{item.getCode()},
                                                    locale)));

                        }).orElseGet(() -> {
                            log.info("Brand not found {}", itemRequestDTO.getBrand());
                            return ResponseEntity.ok().body(responseUtil.error(null, 1021, messageSource.getMessage(ResponseMessageUtil.BRAND_CODE_NOT_FOUND, null, locale)));
                        })).orElseGet(() -> {
                    log.info("Category not found {}", itemRequestDTO.getCategory());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1020, messageSource.getMessage(ResponseMessageUtil.CATEGORY_CODE_NOT_FOUND, null, locale)));
                });

            }).orElseGet(() -> {
                log.info("Item update not found {}", itemRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1014, messageSource.getMessage(ResponseMessageUtil.ITEM_NOT_FOUND_BY_ID, new Object[]{itemRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(ItemRequestDTO itemRequestDTO, Locale locale) {
        try {
            log.info("Item delete {}", itemRequestDTO);

            return itemRepository.findByIdAndStatusNot(itemRequestDTO.getId(), Status.DELETE).map(item -> {
                item.setStatus(Status.DELETE);
                itemRepository.saveAndFlush(item);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.ITEM_DELETE_SUCCESS,    new Object[]{item.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Item delete not found {}", itemRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1021, messageSource.getMessage(ResponseMessageUtil.ITEM_NOT_FOUND_BY_ID, new Object[]{itemRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> getNextSequenceCode(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Get sequence item code {}", channelRequestDTO);
            String nextItemNumber = getNextItemNumber();
            return ResponseEntity.ok().body(responseUtil.success(nextItemNumber, messageSource.getMessage(ResponseMessageUtil.ITEM_CODE_FETCH_SUCCESS, null, locale)));

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Transactional
    public String getNextItemNumber() {
        log.info("Fetching next item number from sequence");
        ItemSequence seq = itemSequenceRepository.findById(1).orElseThrow();
        long next = seq.getNextVal();
        log.info("Current sequence value: {}", next);
        seq.setNextVal(next + 1);
        itemSequenceRepository.saveAndFlush(seq);
        String padded = String.format("%06d", next);
        log.info("Returning padded item number: {}", padded);
        return "ITEM-" + padded;
    }
}
