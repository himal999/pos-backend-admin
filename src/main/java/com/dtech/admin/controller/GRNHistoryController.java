package com.dtech.admin.controller;

import com.dtech.admin.dto.request.*;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.GRNHistoryRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.GRNHistorySearchDTO;
import com.dtech.admin.service.GRNHistoryService;
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
@RequestMapping(path = "api/v1/grn-history")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GRNHistoryController {

    @Autowired
    private final GRNHistoryService grnHistoryService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN history reference data find request request ",notes = "GRN history reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("GRN history reference data request reference data controller {} ", channelRequestValidatorDTO);
        return grnHistoryService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN history filter list request request ",notes = "GRN history filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<GRNHistorySearchDTO> paginationRequest, Locale locale) {
        log.info("GRN history filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<GRNHistorySearchDTO>>(){}.getType();
        return grnHistoryService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN history find by ID request request ",notes = "GRN history find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid GRNHistoryRequestValidatorDTO grnHistoryRequestValidatorDTO, Locale locale) {
        log.info("GRN history find by ID request controller {} ", grnHistoryRequestValidatorDTO);
        return grnHistoryService.view(gson.fromJson(gson.toJson(grnHistoryRequestValidatorDTO), GRNHistoryRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle GRN history update request request ",notes = "GRN history update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid GRNHistoryRequestValidatorDTO grnHistoryRequestValidatorDTO, Locale locale) {
        log.info("GRN history update request controller {} ", grnHistoryRequestValidatorDTO);
        return grnHistoryService.update(gson.fromJson(gson.toJson(grnHistoryRequestValidatorDTO), GRNHistoryRequestDTO.class), locale);
    }

}
