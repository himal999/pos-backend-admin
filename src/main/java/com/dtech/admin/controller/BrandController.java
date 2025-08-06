package com.dtech.admin.controller;

import com.dtech.admin.dto.request.BrandRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.validator.BrandRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.CommonSearchDTO;
import com.dtech.admin.service.BrandService;
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
@RequestMapping(path = "api/v1/item-brand")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BrandController {

    @Autowired
    private final BrandService brandService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand reference data find request request ",notes = "Brand reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Brand reference data request reference data controller {} ", channelRequestValidatorDTO);
        return brandService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand filter list request request ",notes = "Brand filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<CommonSearchDTO> paginationRequest, Locale locale) {
        log.info("Brand filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<CommonSearchDTO>>(){}.getType();
        return brandService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand add request request ",notes = "Brand add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody @Validated(OnAdd.class) @Valid BrandRequestValidatorDTO brandRequestValidatorDTO, Locale locale) {
        log.info("Brand add request controller {} ", brandRequestValidatorDTO);
        return brandService.add(gson.fromJson(gson.toJson(brandRequestValidatorDTO), BrandRequestDTO.class), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand find by ID request request ",notes = "Brand find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid BrandRequestValidatorDTO brandRequestValidatorDTO, Locale locale) {
        log.info("Brand find by ID request controller {} ", brandRequestValidatorDTO);
        return brandService.view(gson.fromJson(gson.toJson(brandRequestValidatorDTO), BrandRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand update request request ",notes = "Brand update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid BrandRequestValidatorDTO brandRequestValidatorDTO, Locale locale) {
        log.info("Brand update request controller {} ", brandRequestValidatorDTO);
        return brandService.update(gson.fromJson(gson.toJson(brandRequestValidatorDTO), BrandRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle brand delete request request ",notes = "Brand delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestBody @Validated(OnDelete.class) @Valid  BrandRequestValidatorDTO brandRequestValidatorDTO, Locale locale) {
        log.info("Brand delete request  controller {} ", brandRequestValidatorDTO);
        return brandService.delete(gson.fromJson(gson.toJson(brandRequestValidatorDTO), BrandRequestDTO.class), locale);
    }
}
