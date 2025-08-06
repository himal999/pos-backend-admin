package com.dtech.admin.service;

import com.dtech.admin.dto.request.BrandRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.CommonSearchDTO;
import org.springframework.http.ResponseEntity;
import java.util.Locale;

public interface BrandService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CommonSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(BrandRequestDTO brandRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(BrandRequestDTO brandRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(BrandRequestDTO brandRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> delete(BrandRequestDTO brandRequestDTO, Locale locale);
}
