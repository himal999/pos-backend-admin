package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.TransferRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.service.TransferService;
import com.dtech.admin.validator.OnDelete;
import com.dtech.admin.validator.OnGet;
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
@RequestMapping(path = "api/v1/transfer")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransferController {

    @Autowired
    private final TransferService transferService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item transfer reference data find request request ",notes = "Item transfer reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Item transfer reference data request reference data controller {} ", channelRequestValidatorDTO);
        return transferService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle  item transfer filter list request request ",notes = "Item transfer filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<TransferSearchDTO> paginationRequest, Locale locale) {
        log.info("Item transfer filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<TransferSearchDTO>>(){}.getType();
        return transferService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle category find by ID request request ",notes = "Category find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid TransferRequestValidatorDTO transferRequestValidatorDTO, Locale locale) {
        log.info("Category find by ID request controller {} ", transferRequestValidatorDTO);
        return transferService.view(gson.fromJson(gson.toJson(transferRequestValidatorDTO), TransferRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item transfer cancel request request ",notes = "Item transfer cancel request success or failed")
    public ResponseEntity<ApiResponse<Object>> transferCancel(@RequestBody @Validated(OnDelete.class) @Valid TransferRequestValidatorDTO transferRequestValidatorDTO, Locale locale) {
        log.info("Item transfer cancel request  controller {} ", transferRequestValidatorDTO);
        return transferService.transferCancel(gson.fromJson(gson.toJson(transferRequestValidatorDTO), TransferRequestDTO.class), locale);
    }

}
