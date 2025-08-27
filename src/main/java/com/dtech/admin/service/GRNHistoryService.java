package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.GRNHistoryRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.GRNHistorySearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface GRNHistoryService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<GRNHistorySearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(GRNHistoryRequestDTO grnHistoryRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(GRNHistoryRequestDTO grnHistoryRequestDTO, Locale locale);
}
