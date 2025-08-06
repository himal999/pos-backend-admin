package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.LocationRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.LocationSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface LocationService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<LocationSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(LocationRequestDTO locationRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(LocationRequestDTO locationRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(LocationRequestDTO locationRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> delete(LocationRequestDTO locationRequestDTO, Locale locale);
}
