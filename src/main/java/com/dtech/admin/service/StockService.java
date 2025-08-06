package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.StockRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.StockSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface StockService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<StockSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(StockRequestDTO stockRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(StockRequestDTO stockRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> stockEnableDisable(StockRequestDTO stockRequestDTO, Locale locale);
}
