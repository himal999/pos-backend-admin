package com.dtech.admin.controller;

import com.dtech.admin.dto.request.CashierBalanceRequestDTO;
import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.dto.request.validator.CashierBalanceRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.service.CashBookService;
import com.dtech.admin.validator.OnGet;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path = "api/v1/cash-book")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CashBookController {

    @Autowired
    private final CashBookService cashBookService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashbook reference data find request request ",notes = "Cashbook reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Cashbook reference data request reference data controller {} ", channelRequestValidatorDTO);
        return cashBookService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/cashier-balance",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashier balance request request ",notes = "Cashier balance request success or failed")
    public ResponseEntity<ApiResponse<Object>> cashierBalance(@RequestBody  @Valid  CashierBalanceRequestValidatorDTO cashierBalanceRequestValidatorDTO, Locale locale) throws Exception {
        log.info("Cashier balance request controller {} ", cashierBalanceRequestValidatorDTO);
        return cashBookService.cashierBalance(gson.fromJson(gson.toJson(cashierBalanceRequestValidatorDTO), CashierBalanceRequestDTO.class), locale);
    }

    @PostMapping(path = "/cashier-balance-view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashier balance view request request ",notes = "Cashier balance view request success or failed")
    public ResponseEntity<ApiResponse<Object>> cashierBalanceView(@RequestBody @Validated(OnGet.class) @Valid  CashierBalanceRequestValidatorDTO cashierBalanceRequestValidatorDTO, Locale locale) throws Exception {
        log.info("Cashier balance view request controller {} ", cashierBalanceRequestValidatorDTO);
        return cashBookService.cashierBalanceView(gson.fromJson(gson.toJson(cashierBalanceRequestValidatorDTO), CashierBalanceRequestDTO.class), locale);
    }

}
