package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.LocationRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.AuthorizationTaskResponseDTO;
import com.dtech.admin.dto.response.LocationResponseDTO;
import com.dtech.admin.dto.search.LocationSearchDTO;
import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.LocationMapper;
import com.dtech.admin.model.Location;
import com.dtech.admin.repository.LocationRepository;
import com.dtech.admin.service.LocationService;
import com.dtech.admin.specifications.LocationSpecification;
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
public class LocationServiceImpl implements LocationService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Location page reference data {} ", channelRequestDTO);
            Map<String,Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.LCMP.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> locations = Arrays.stream(LocationType.values())
                    .map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("locations", locations);
            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.LCMP.name()}, locale)));

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<LocationSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Location filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Location> locations = Objects.nonNull(paginationRequest.getSearch()) ?
                    locationRepository.findAll(LocationSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    locationRepository.findAll(LocationSpecification.getSpecification(), pageable);
            log.info("Location filter records {}", locations);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    locationRepository.count(LocationSpecification.getSpecification(paginationRequest.getSearch())) :
                    locationRepository.count(LocationSpecification.getSpecification());
            log.info("Location filter records map start");
            List<LocationResponseDTO> responseDTOList = locations.stream()
                    .map(LocationMapper::mapLocationMapper).toList();
            log.info("Location filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<LocationResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.LOCATION_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(LocationRequestDTO locationRequestDTO, Locale locale) {
        try {
            log.info("Location add {}", locationRequestDTO);

            boolean exists = locationRepository.existsByCodeAndStatusNot(locationRequestDTO.getCode(),Status.DELETE);

            if (exists) {
                log.info("Location code {} already exists", locationRequestDTO.getCode());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1018,
                                messageSource.getMessage(
                                        ResponseMessageUtil.LOCATION_CODE_ALREADY_EXISTS,
                                        new Object[]{locationRequestDTO.getCode()},
                                        locale
                                )
                        )
                );
            }

            Location location = com.dtech.admin.mapper.dtoToEntity.LocationMapper.mapLocation(locationRequestDTO);
            locationRepository.saveAndFlush(location);
            log.info("Location added {}", location);

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            null,
                            messageSource.getMessage(
                                    ResponseMessageUtil.LOCATION_ADDED_SUCCESS,
                                    new Object[]{location.getCode()},
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
    public ResponseEntity<ApiResponse<Object>> view(LocationRequestDTO locationRequestDTO, Locale locale) {
        try {
            log.info("Location view {}", locationRequestDTO);
            return locationRepository.findByIdAndStatusNot(locationRequestDTO.getId(),Status.DELETE).map(location -> {
                LocationResponseDTO locationResponseDTO = LocationMapper.mapLocationMapper(location);
                return ResponseEntity.ok().body(responseUtil.success((Object) locationResponseDTO, messageSource.getMessage(ResponseMessageUtil.LOCATION_FOUND_BY_ID, new Object[]{locationResponseDTO.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Location view not found {}", locationRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1018, messageSource.getMessage(ResponseMessageUtil.LOCATION_NOT_FOUND_BY_ID, new Object[]{locationRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(LocationRequestDTO locationRequestDTO, Locale locale) {
        try {
            log.info("Location update {}", locationRequestDTO);
            return locationRepository.findByIdAndStatusNot(locationRequestDTO.getId(),Status.DELETE).map(location -> {

                String newModel = new StringBuilder()
                        .append(locationRequestDTO.getDescription())
                        .append("|")
                        .append(locationRequestDTO.getCity())
                        .append("|")
                        .append(locationRequestDTO.getContactNumber())
                        .append("|")
                        .append(locationRequestDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(location.getDescription())
                        .append("|")
                        .append(location.getCity())
                        .append("|")
                        .append(location.getContactNumber())
                        .append("|")
                        .append(location.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Location update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1019, messageSource.getMessage(ResponseMessageUtil.LOCATION_VALUES_NOT_CHANGING, null, locale)));
                }

                log.info("Location update old audit end");
                location.setStatus(Status.valueOf(locationRequestDTO.getStatus()));
                location.setDescription(locationRequestDTO.getDescription());
                location.setContactNumber(locationRequestDTO.getContactNumber());
                location.setCity(locationRequestDTO.getCity());
                locationRepository.saveAndFlush(location);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.LOCATION_UPDATE_SUCCESS,    new Object[]{location.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Location update not found {}", locationRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1018, messageSource.getMessage(ResponseMessageUtil.LOCATION_NOT_FOUND_BY_ID, new Object[]{locationRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(LocationRequestDTO locationRequestDTO, Locale locale) {
        try {
            log.info("Location delete {}", locationRequestDTO);

            return locationRepository.findByIdAndStatusNot(locationRequestDTO.getId(),Status.DELETE).map(location -> {
                location.setStatus(Status.DELETE);
                locationRepository.saveAndFlush(location);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.LOCATION_DELETE_SUCCESS,  new Object[]{location.getCode()}, locale)));
            }).orElseGet(() -> {
                log.info("Location delete not found {}", locationRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1018, messageSource.getMessage(ResponseMessageUtil.LOCATION_NOT_FOUND_BY_ID, new Object[]{locationRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }
}
