package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.TransferRequestListDTO;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.TransferRequestListValidatorDTO;
import com.dtech.admin.dto.request.validator.TransferRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.service.ItemTransferService;
import com.dtech.admin.validator.OnAdd;
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
@RequestMapping(path = "api/v1/item-transfer")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItemTransferController {

    @Autowired
    private final ItemTransferService itemTransferService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item transfer reference data find request request ",notes = "Item transfer reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Item transfer reference data request reference data controller {} ", channelRequestValidatorDTO);
        return itemTransferService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle  item transfer filter list request request ",notes = "Item transfer filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<TransferSearchDTO> paginationRequest, Locale locale) {
        log.info("Item transfer filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<TransferSearchDTO>>(){}.getType();
        return itemTransferService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/transfer",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle  item transfer request request ",notes = "Item transfer request success or failed")
    public ResponseEntity<ApiResponse<Object>> transfer(@RequestBody @Validated(OnAdd.class) @Valid TransferRequestListValidatorDTO transferRequestValidatorDTO, Locale locale) {
        log.info("Item transfer request controller {} ", transferRequestValidatorDTO);
        return itemTransferService.transfer(gson.fromJson(gson.toJson(transferRequestValidatorDTO), TransferRequestListDTO.class), locale);
    }

}
