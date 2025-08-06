package com.dtech.admin.controller;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.validator.ChannelRequestValidatorDTO;
import com.dtech.admin.dto.request.validator.ItemRequestValidatorDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.ItemSearchDTO;
import com.dtech.admin.service.ItemService;
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
@RequestMapping(path = "api/v1/item")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final Gson gson;

    @PostMapping(path = "/reference-data",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item reference data find request request ",notes = "Item reference data request success or failed")
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Item reference data request reference data controller {} ", channelRequestValidatorDTO);
        return itemService.getReferenceDate(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/filter-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item filter list request request ",notes = "Item filter list request success or failed")
    public ResponseEntity<ApiResponse<Object>> filterList(@RequestBody @Valid PaginationRequest<ItemSearchDTO> paginationRequest, Locale locale) {
        log.info("Item filter list request controller {} ", paginationRequest);
        Type paginationRequestType = new TypeToken<PaginationRequest<ItemSearchDTO>>(){}.getType();
        return itemService.filterList(gson.fromJson(gson.toJson(paginationRequest), paginationRequestType), locale);
    }

    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item add request request ",notes = "Item add request success or failed")
    public ResponseEntity<ApiResponse<Object>> add(@RequestBody @Validated(OnAdd.class) @Valid ItemRequestValidatorDTO itemRequestValidatorDTO, Locale locale) {
        log.info("Item add request controller {} ", itemRequestValidatorDTO);
        return itemService.add(gson.fromJson(gson.toJson(itemRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item find by ID request request ",notes = "Item find by ID request success or failed")
    public ResponseEntity<ApiResponse<Object>> view(@RequestBody @Validated(OnGet.class) @Valid ItemRequestValidatorDTO itemRequestValidatorDTO, Locale locale) {
        log.info("Item find by ID request controller {} ", itemRequestValidatorDTO);
        return itemService.view(gson.fromJson(gson.toJson(itemRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item update request request ",notes = "Item update request success or failed")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody @Validated(OnUpdate.class) @Valid ItemRequestValidatorDTO itemRequestValidatorDTO, Locale locale) {
        log.info("Item update request controller {} ", itemRequestValidatorDTO);
        return itemService.update(gson.fromJson(gson.toJson(itemRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/delete",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item delete request request ",notes = "Item delete request success or failed")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestBody @Validated(OnDelete.class) @Valid  ItemRequestValidatorDTO itemRequestValidatorDTO, Locale locale) {
        log.info("Item delete request  controller {} ", itemRequestValidatorDTO);
        return itemService.delete(gson.fromJson(gson.toJson(itemRequestValidatorDTO), ItemRequestDTO.class), locale);
    }

    @PostMapping(path = "/item-sequence",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Handle item sequence request request ",notes = "Get item sequence request success or failed")
    public ResponseEntity<ApiResponse<Object>> getNextSequenceCode(@RequestBody @Valid ChannelRequestValidatorDTO channelRequestValidatorDTO, Locale locale) {
        log.info("Get item sequence request controller {} ", channelRequestValidatorDTO);
        return itemService.getNextSequenceCode(gson.fromJson(gson.toJson(channelRequestValidatorDTO), ChannelRequestDTO.class), locale);
    }
}
