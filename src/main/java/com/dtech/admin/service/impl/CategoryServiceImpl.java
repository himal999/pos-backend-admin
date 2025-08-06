package com.dtech.admin.service.impl;

import com.dtech.admin.dto.CommonResponseDTO;
import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.CategoryRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.search.CommonSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.CategoryMapper;
import com.dtech.admin.model.Category;
import com.dtech.admin.repository.CategoryRepository;
import com.dtech.admin.service.CategoryService;
import com.dtech.admin.specifications.CategorySpecification;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.PaginationUtil;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Category page reference data {} ", channelRequestDTO);
            Map<String,Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.CAMP.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.CAMP.name()}, locale)));

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CommonSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Category filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Category> categories = Objects.nonNull(paginationRequest.getSearch()) ?
                    categoryRepository.findAll(CategorySpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    categoryRepository.findAll(CategorySpecification.getSpecification(), pageable);
            log.info("Category filter records {}", categories);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    categoryRepository.count(CategorySpecification.getSpecification(paginationRequest.getSearch())) :
                    categoryRepository.count(CategorySpecification.getSpecification());
            log.info("Category filter records map start");
            List<CommonResponseDTO> responseDTOList = categories.stream()
                    .map(CategoryMapper::mapCategoryMapper).toList();
            log.info("Category filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<CommonResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.CATEGORY_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(CategoryRequestDTO categoryRequest, Locale locale) {
        try {
            log.info("Category add {}", categoryRequest);

            boolean exists = categoryRepository.existsByCodeAndStatusNot(categoryRequest.getCode(),Status.DELETE);

            if (exists) {
                log.info("Category code {} already exists", categoryRequest.getCode());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1010,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CATEGORY_CODE_ALREADY_EXISTS,
                                        new Object[]{categoryRequest.getCode()},
                                        locale
                                )
                        )
                );
            }

            Category category = com.dtech.admin.mapper.dtoToEntity.CategoryMapper.mapCategory(categoryRequest);
            categoryRepository.saveAndFlush(category);
            log.info("Category added {}", category);

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            null,
                            messageSource.getMessage(
                                    ResponseMessageUtil.CATEGORY_ADDED_SUCCESS,
                                    new Object[]{categoryRequest.getCode()},
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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(CategoryRequestDTO categoryRequest, Locale locale) {
        try {
            log.info("Category view {}", categoryRequest);
            return categoryRepository.findByIdAndStatusNot(categoryRequest.getId(),Status.DELETE).map(ca -> {
                CommonResponseDTO category = CategoryMapper.mapCategoryMapper(ca);
                return ResponseEntity.ok().body(responseUtil.success((Object) category, messageSource.getMessage(ResponseMessageUtil.CATEGORY_FOUND_BY_ID, new Object[]{category.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Category view not found {}", categoryRequest.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1012, messageSource.getMessage(ResponseMessageUtil.CATEGORY_NOT_FOUND_BY_ID, new Object[]{categoryRequest.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(CategoryRequestDTO categoryRequest, Locale locale) {
        try {
            log.info("Category update {}", categoryRequest);
            return categoryRepository.findByIdAndStatusNot(categoryRequest.getId(),Status.DELETE).map(category -> {

                String newModel = new StringBuilder()
                        .append(categoryRequest.getDescription())
                        .append("|")
                        .append(categoryRequest.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(category.getDescription())
                        .append("|")
                        .append(category.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Category update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1011, messageSource.getMessage(ResponseMessageUtil.CATEGORY_VALUES_NOT_CHANGING, null, locale)));
                }

                log.info("Category update old audit end");
                category.setStatus(Status.valueOf(categoryRequest.getStatus()));
                category.setDescription(categoryRequest.getDescription());
                categoryRepository.saveAndFlush(category);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.CATEGORY_UPDATE_SUCCESS, new Object[]{category.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Category update not found {}", categoryRequest.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1012, messageSource.getMessage(ResponseMessageUtil.CATEGORY_NOT_FOUND_BY_ID, new Object[]{categoryRequest.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(CategoryRequestDTO categoryRequest, Locale locale) {
        try {
            log.info("Category delete {}", categoryRequest);

            return categoryRepository.findByIdAndStatusNot(categoryRequest.getId(),Status.DELETE).map(category -> {
                category.setStatus(Status.DELETE);
                categoryRepository.saveAndFlush(category);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.CATEGORY_DELETE_SUCCESS, new Object[]{category.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Category delete not found {}", categoryRequest.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1012, messageSource.getMessage(ResponseMessageUtil.CATEGORY_NOT_FOUND_BY_ID, new Object[]{categoryRequest.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }
}
