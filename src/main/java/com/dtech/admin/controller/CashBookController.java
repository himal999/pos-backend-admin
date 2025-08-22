package com.dtech.admin.controller;

import com.dtech.admin.dto.request.CashierBalanceActionRequestDTO;
import com.dtech.admin.dto.request.CashierBalanceRequestDTO;
import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.dto.request.validator.CashierBalanceActionRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.CashierBalanceRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.service.CashBookService;
import com.dtech.admin.validator.OnAdd;
import com.dtech.admin.validator.OnDelete;
import com.dtech.admin.validator.OnGet;
import com.dtech.admin.validator.OnUpdate;
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

    @PostMapping(path = "/edit",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashier balance edit request request ",notes = "Cashier balance edit request success or failed")
    public ResponseEntity<ApiResponse<Object>> edit(@RequestBody @Validated(OnUpdate.class) @Valid CashierBalanceActionRequestValidatorDTO cashierBalanceActionRequestValidatorDTO, Locale locale) throws Exception {
        log.info("Cashier balance edit request controller {} ", cashierBalanceActionRequestValidatorDTO);
        return cashBookService.edit(gson.fromJson(gson.toJson(cashierBalanceActionRequestValidatorDTO), CashierBalanceActionRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashier balance delete request request ",notes = "Cashier balance delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestBody @Validated(OnDelete.class) @Valid CashierBalanceActionRequestValidatorDTO cashierBalanceActionRequestValidatorDTO, Locale locale) throws Exception {
        log.info("Cashier balance delete request controller {} ", cashierBalanceActionRequestValidatorDTO);
        return cashBookService.delete(gson.fromJson(gson.toJson(cashierBalanceActionRequestValidatorDTO), CashierBalanceActionRequestDTO.class), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle cashier balance add request request ",notes = "Cashier balance add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody  @Validated(OnAdd.class) @Valid CashierBalanceActionRequestValidatorDTO cashierBalanceActionRequestValidatorDTO, Locale locale) throws Exception {
        log.info("Cashier balance add request controller {} ", cashierBalanceActionRequestValidatorDTO);
        return cashBookService.add(gson.fromJson(gson.toJson(cashierBalanceActionRequestValidatorDTO), CashierBalanceActionRequestDTO.class), locale);
    }


}
