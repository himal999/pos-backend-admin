package com.dtech.admin.service.impl;

import com.dtech.admin.dto.CommonResponseDTO;
import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.BrandRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.search.CommonSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.BrandMapper;
import com.dtech.admin.model.Brand;
import com.dtech.admin.repository.BrandRepository;
import com.dtech.admin.service.BrandService;
import com.dtech.admin.specifications.BrandSpecification;
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
public class BrandServiceImpl implements BrandService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Brand page reference data {} ", channelRequestDTO);
            Map<String,Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.BRMP.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.BRMP.name()}, locale)));

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CommonSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Brand filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Brand> brands = Objects.nonNull(paginationRequest.getSearch()) ?
                    brandRepository.findAll(BrandSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    brandRepository.findAll(BrandSpecification.getSpecification(), pageable);
            log.info("Brand filter records {}", brands);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    brandRepository.count(BrandSpecification.getSpecification(paginationRequest.getSearch())) :
                    brandRepository.count(BrandSpecification.getSpecification());
            log.info("Brand filter records map start");
            List<CommonResponseDTO> responseDTOList = brands.stream()
                    .map(BrandMapper::mapBrandMapper).toList();
            log.info("Brand filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<CommonResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.BRAND_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(BrandRequestDTO brandRequestDTO, Locale locale) {
        try {
            log.info("Brand add {}", brandRequestDTO);

            boolean exists = brandRepository.existsByCodeAndStatusNot(brandRequestDTO.getCode(),Status.DELETE);

            if (exists) {
                log.info("Brand code {} already exists", brandRequestDTO.getCode());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1013,
                                messageSource.getMessage(
                                        ResponseMessageUtil.BRAND_CODE_ALREADY_EXISTS,
                                        new Object[]{brandRequestDTO.getCode()},
                                        locale
                                )
                        )
                );
            }

            Brand brand = com.dtech.admin.mapper.dtoToEntity.BrandMapper.mapBrand(brandRequestDTO);
            brandRepository.saveAndFlush(brand);
            log.info("Brand added {}", brand);

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            null,
                            messageSource.getMessage(
                                    ResponseMessageUtil.BRAND_ADDED_SUCCESS,
                                    new Object[]{brandRequestDTO.getCode()},
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
    public ResponseEntity<ApiResponse<Object>> view(BrandRequestDTO brandRequestDTO, Locale locale) {
        try {
            log.info("Brand view {}", brandRequestDTO);
            return brandRepository.findByIdAndStatusNot(brandRequestDTO.getId(),Status.DELETE).map(brand -> {
                CommonResponseDTO brandMapper = BrandMapper.mapBrandMapper(brand);
                return ResponseEntity.ok().body(responseUtil.success((Object) brandMapper, messageSource.getMessage(ResponseMessageUtil.BRAND_FOUND_BY_ID, new Object[]{brandMapper.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Brand view not found {}", brandRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1014, messageSource.getMessage(ResponseMessageUtil.BRAND_NOT_FOUND_BY_ID, new Object[]{brandRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(BrandRequestDTO brandRequestDTO, Locale locale) {
        try {
            log.info("Category update {}", brandRequestDTO);
            return brandRepository.findByIdAndStatusNot(brandRequestDTO.getId(),Status.DELETE).map(brand -> {

                String newModel = new StringBuilder()
                        .append(brandRequestDTO.getDescription())
                        .append("|")
                        .append(brandRequestDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(brand.getDescription())
                        .append("|")
                        .append(brand.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Brand update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1015, messageSource.getMessage(ResponseMessageUtil.BRAND_VALUES_NOT_CHANGING, null, locale)));
                }

                log.info("Brand update old audit end");
                brand.setStatus(Status.valueOf(brandRequestDTO.getStatus()));
                brand.setDescription(brandRequestDTO.getDescription());
                brandRepository.saveAndFlush(brand);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.BRAND_UPDATE_SUCCESS, new Object[]{brand.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Brand update not found {}", brandRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1014, messageSource.getMessage(ResponseMessageUtil.BRAND_NOT_FOUND_BY_ID, new Object[]{brandRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(BrandRequestDTO brandRequestDTO, Locale locale) {
        try {
            log.info("Brand delete {}", brandRequestDTO);

            return brandRepository.findByIdAndStatusNot(brandRequestDTO.getId(),Status.DELETE).map(brand -> {
                brand.setStatus(Status.DELETE);
                brandRepository.saveAndFlush(brand);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.BRAND_DELETE_SUCCESS, new Object[]{brand.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Brand delete not found {}", brandRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1014, messageSource.getMessage(ResponseMessageUtil.BRAND_NOT_FOUND_BY_ID, new Object[]{brandRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }
}
