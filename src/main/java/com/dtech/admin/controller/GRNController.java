package com.dtech.admin.controller;

import com.dtech.admin.dto.request.*;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.GRNRequestItemValidatorDTO;
import com.dtech.admin.dto.request.validator.GRNRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.GRNSearchDTO;
import com.dtech.admin.service.GRNService;
import com.dtech.admin.validator.*;
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
@RequestMapping(path = "api/v1/grn")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GRNController {

    @Autowired
    private final GRNService grnService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN reference data find request request ",notes = "GRN reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("GRN reference data request reference data controller {} ", channelRequestValidatorDTO);
        return grnService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN filter list request request ",notes = "GRN filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<GRNSearchDTO> paginationRequest, Locale locale) {
        log.info("GRN filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<GRNSearchDTO>>(){}.getType();
        return grnService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN add request request ",notes = "GRN add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody @Validated(OnAdd.class) @Valid GRNRequestValidatorDTO grnRequestValidatorDTO, Locale locale) {
        log.info("GRN add request controller {} ", grnRequestValidatorDTO);
        return grnService.add(gson.fromJson(gson.toJson(grnRequestValidatorDTO), GRNRequestDTO.class), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN find by ID request request ",notes = "GRN find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid GRNRequestItemValidatorDTO grnRequestItemValidatorDTO, Locale locale) {
        log.info("GRN find by ID request controller {} ", grnRequestItemValidatorDTO);
        return grnService.view(gson.fromJson(gson.toJson(grnRequestItemValidatorDTO), GRNRequestItemDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN update request request ",notes = "GRN update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid GRNRequestItemValidatorDTO grnRequestItemValidatorDTO, Locale locale) {
        log.info("GRN update request controller {} ", grnRequestItemValidatorDTO);
        return grnService.update(gson.fromJson(gson.toJson(grnRequestItemValidatorDTO), GRNRequestItemDTO.class), locale);
    }

    @PostMapping(path = "/disable",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN temporary request request ",notes = "GRN temporary request success or failed")
    public ResponseEntity<ApiResponse<Object>> stockEnableDisable(@RequestBody @Validated(OnGRNItemDisable.class) @Valid GRNRequestItemValidatorDTO grnRequestItemValidatorDTO, Locale locale) {
        log.info("GRN temporary request  controller {} ", grnRequestItemValidatorDTO);
        return grnService.stockEnableDisable(gson.fromJson(gson.toJson(grnRequestItemValidatorDTO), GRNRequestItemDTO.class), locale);
    }
}
