package com.dtech.admin.controller;

import com.dtech.admin.dto.request.*;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.StockRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.StockSearchDTO;
import com.dtech.admin.service.StockService;
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
@RequestMapping(path = "api/v1/stock")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private final StockService stockService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle stock reference data find request request ",notes = "Stock reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Stock reference data request reference data controller {} ", channelRequestValidatorDTO);
        return stockService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle stock filter list request request ",notes = "Stock filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<StockSearchDTO> paginationRequest, Locale locale) {
        log.info("Stock filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<StockSearchDTO>>(){}.getType();
        return stockService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle stock find by ID request request ",notes = "Stock find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid StockRequestValidatorDTO stockRequestValidatorDTO, Locale locale) {
        log.info("Stock find by ID request controller {} ", stockRequestValidatorDTO);
        return stockService.view(gson.fromJson(gson.toJson(stockRequestValidatorDTO), StockRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle stock update request request ",notes = "Stock update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid StockRequestValidatorDTO stockRequestValidatorDTO, Locale locale) {
        log.info("Stock update request controller {} ", stockRequestValidatorDTO);
        return stockService.update(gson.fromJson(gson.toJson(stockRequestValidatorDTO), StockRequestDTO.class), locale);
    }

    @PostMapping(path = "/disable",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle stock delete request request ",notes = "Stock temporary disable request success or failed")
    public ResponseEntity<ApiResponse<Object>> stockEnableDisable(@RequestBody @Validated(OnGRNItemDisable.class) @Valid StockRequestValidatorDTO stockRequestValidatorDTO, Locale locale) {
        log.info("Stock temporary disable request  controller {} ", stockRequestValidatorDTO);
        return stockService.stockEnableDisable(gson.fromJson(gson.toJson(stockRequestValidatorDTO), StockRequestDTO.class), locale);
    }


}
