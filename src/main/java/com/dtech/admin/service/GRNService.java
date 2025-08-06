package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.GRNRequestDTO;
import com.dtech.admin.dto.request.GRNRequestItemDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.GRNSearchDTO;
import org.springframework.http.ResponseEntity;
import java.util.Locale;

public interface GRNService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<GRNSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(GRNRequestDTO grnRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(GRNRequestItemDTO grnRequestItemDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(GRNRequestItemDTO grnRequestItemDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> stockEnableDisable(GRNRequestItemDTO grnRequestItemDTO, Locale locale);
}
