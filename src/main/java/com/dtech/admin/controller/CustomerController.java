package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.CustomerRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.CustomerRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.CustomerSearchDTO;
import com.dtech.admin.service.CustomerService;
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
@RequestMapping(path = "api/v1/customer")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer reference data find request request ",notes = "Customer reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Customer reference data request reference data controller {} ", channelRequestValidatorDTO);
        return customerService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer filter list request request ",notes = "Customer filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        log.info("Customer filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<CustomerSearchDTO>>(){}.getType();
        return customerService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/filter-list/billing",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer billing filter list request request ",notes = "Customer billing filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> billingHistory(@RequestBody @Valid PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        log.info("Customer billing filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<CustomerSearchDTO>>(){}.getType();
        return customerService.billingHistory(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/billing/item-details",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle billing items find by ID request request ",notes = "Customer billing items find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> billingItemDetails(@RequestBody @Validated(OnGet.class) @Valid CustomerRequestValidatorDTO customerRequestValidatorDTO, Locale locale) {
        log.info("Customer billing items find by ID request controller {} ", customerRequestValidatorDTO);
        return customerService.billingItemDetails(gson.fromJson(gson.toJson(customerRequestValidatorDTO), CustomerRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list/settle-payment",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer settle payment filter list request request ",notes = "Customer settle payment filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> settlePaymentHistory(@RequestBody @Valid PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        log.info("Customer settle payment filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<CustomerSearchDTO>>(){}.getType();
        return customerService.settlePaymentHistory(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/update/basic-info",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer basic info update request request ",notes = "Customer basic info update request success or failed")
    public ResponseEntity<ApiResponse<Object>> basicEditInfo(@RequestBody @Validated(BasicInfo.class) @Valid CustomerRequestValidatorDTO customerRequestValidatorDTO, Locale locale) {
        log.info("Customer basic info update request controller {} ", customerRequestValidatorDTO);
        return customerService.basicEditInfo(gson.fromJson(gson.toJson(customerRequestValidatorDTO), CustomerRequestDTO.class), locale);
    }

    @PostMapping(path = "/update/credit-limit",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer credit  update request request ",notes = "Customer credit update request success or failed")
    public ResponseEntity<ApiResponse<Object>> creditLimitUpdate(@RequestBody @Validated(CreditLimit.class) @Valid CustomerRequestValidatorDTO customerRequestValidatorDTO, Locale locale) {
        log.info("Customer credit update request controller {} ", customerRequestValidatorDTO);
        return customerService.creditLimitUpdate(gson.fromJson(gson.toJson(customerRequestValidatorDTO), CustomerRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer delete request request ",notes = "Customer delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> customerPermanentRemove(@RequestBody @Validated(OnDelete.class) @Valid CustomerRequestValidatorDTO customerRequestValidatorDTO, Locale locale) {
        log.info("Customer delete request controller {} ", customerRequestValidatorDTO);
        return customerService.customerPermanentRemove(gson.fromJson(gson.toJson(customerRequestValidatorDTO), CustomerRequestDTO.class), locale);
    }

    @PostMapping(path = "/credit-cash-update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle customer credit cash update request request ",notes = "Customer credit cash update request success or failed")
    public ResponseEntity<ApiResponse<Object>> customerCashCreditBillingUpdate(@RequestBody @Validated(CreditCashStatus.class) @Valid CustomerRequestValidatorDTO customerRequestValidatorDTO, Locale locale) {
        log.info("Customer credit cash update request controller {} ", customerRequestValidatorDTO);
        return customerService.customerCashCreditBillingUpdate(gson.fromJson(gson.toJson(customerRequestValidatorDTO), CustomerRequestDTO.class), locale);
    }

}
