package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.LocationRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.LocationRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.LocationSearchDTO;
import com.dtech.admin.service.LocationService;
import com.dtech.admin.validator.OnAdd;
import com.dtech.admin.validator.OnDelete;
import com.dtech.admin.validator.OnGet;
import com.dtech.admin.validator.OnUpdate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Locale;

@RestController
@RequestMapping(path = "api/v1/location")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private final LocationService locationService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location reference data find request request ",notes = "Location reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Location reference data request reference data controller {} ", channelRequestValidatorDTO);
        return locationService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location filter list request request ",notes = "Location filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<LocationSearchDTO> paginationRequest, Locale locale) {
        log.info("Location filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<LocationSearchDTO>>(){}.getType();
        return locationService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location add request request ",notes = "Location add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody @Validated(OnAdd.class) @Valid LocationRequestValidatorDTO locationRequestValidatorDTO, Locale locale) {
        log.info("Location add request controller {} ", locationRequestValidatorDTO);
        return locationService.add(gson.fromJson(gson.toJson(locationRequestValidatorDTO), LocationRequestDTO.class), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location find by ID request request ",notes = "Location find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid LocationRequestValidatorDTO locationRequestValidatorDTO, Locale locale) {
        log.info("Location find by ID request controller {} ", locationRequestValidatorDTO);
        return locationService.view(gson.fromJson(gson.toJson(locationRequestValidatorDTO), LocationRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location update request request ",notes = "Location update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid LocationRequestValidatorDTO locationRequestValidatorDTO, Locale locale) {
        log.info("Location update request controller {} ", locationRequestValidatorDTO);
        return locationService.update(gson.fromJson(gson.toJson(locationRequestValidatorDTO), LocationRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle location delete request request ",notes = "Location delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestBody @Validated(OnDelete.class) @Valid LocationRequestValidatorDTO locationRequestValidatorDTO, Locale locale) {
        log.info("Location delete request  controller {} ", locationRequestValidatorDTO);
        return locationService.delete(gson.fromJson(gson.toJson(locationRequestValidatorDTO), LocationRequestDTO.class), locale);
    }

}
