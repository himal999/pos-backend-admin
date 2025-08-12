package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.SupplierPaymentRequestDTO;
import com.dtech.admin.dto.request.SupplierRequestDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.SupplierPaymentRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.SupplierRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.SupplierSearchDTO;
import com.dtech.admin.service.SupplierService;
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
@RequestMapping(path = "api/v1/item-supplier")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {

    @Autowired
    private final SupplierService supplierService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier reference data find request request ",notes = "Supplier reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Supplier reference data request reference data controller {} ", channelRequestValidatorDTO);
        return supplierService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier filter list request request ",notes = "Supplier filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<SupplierSearchDTO> paginationRequest, Locale locale) {
        log.info("Supplier filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<SupplierSearchDTO>>(){}.getType();
        return supplierService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier add request request ",notes = "Supplier add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody @Validated(OnAdd.class) @Valid SupplierRequestValidatorDTO supplierRequestValidatorDTO, Locale locale) {
        log.info("Supplier add request controller {} ", supplierRequestValidatorDTO);
        return supplierService.add(gson.fromJson(gson.toJson(supplierRequestValidatorDTO), SupplierRequestDTO.class), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier find by ID request request ",notes = "Supplier find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid  SupplierRequestValidatorDTO supplierRequestValidatorDTO, Locale locale) {
        log.info("Supplier find by ID request controller {} ", supplierRequestValidatorDTO);
        return supplierService.view(gson.fromJson(gson.toJson(supplierRequestValidatorDTO), SupplierRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier update request request ",notes = "Supplier update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid  SupplierRequestValidatorDTO supplierRequestValidatorDTO, Locale locale) {
        log.info("Supplier update request controller {} ", supplierRequestValidatorDTO);
        return supplierService.update(gson.fromJson(gson.toJson(supplierRequestValidatorDTO), SupplierRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier delete request request ",notes = "Supplier delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestBody @Validated(OnDelete.class) @Valid   SupplierRequestValidatorDTO supplierRequestValidatorDTO, Locale locale) {
        log.info("Supplier delete request  controller {} ", supplierRequestValidatorDTO);
        return supplierService.delete(gson.fromJson(gson.toJson(supplierRequestValidatorDTO), SupplierRequestDTO.class), locale);
    }

    @PostMapping(path = "/add-payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier payment add request", notes = "Supplier payment add request success or failed")
    public ResponseEntity<? extends ApiResponse<? extends Object>> addPayment(@RequestBody @Validated(OnAdd.class) @Valid SupplierPaymentRequestValidatorDTO paymentRequestValidatorDTO, Locale locale) {
        log.info("Supplier payment add request controller {} ", paymentRequestValidatorDTO);
        return supplierService.addPayment(gson.fromJson(gson.toJson(paymentRequestValidatorDTO), SupplierPaymentRequestDTO.class), locale);
    }

    @PostMapping(path = "/balance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle supplier balance retrieval request", notes = "Supplier balance retrieval request success or failed")
    public ResponseEntity<? extends ApiResponse<? extends Object>> getSupplierBalance(@RequestBody @Validated(OnGet.class) @Valid SupplierRequestValidatorDTO supplierRequestValidatorDTO, Locale locale) {
        log.info("Supplier balance request controller {} ", supplierRequestValidatorDTO);
        return supplierService.getSupplierBalance(gson.fromJson(gson.toJson(supplierRequestValidatorDTO), SupplierRequestDTO.class), locale);
    }
}
