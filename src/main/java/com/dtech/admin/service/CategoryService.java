package com.dtech.admin.service;

import com.dtech.admin.dto.request.CategoryRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.CommonSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface CategoryService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CommonSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(CategoryRequestDTO categoryRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(CategoryRequestDTO categoryRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(CategoryRequestDTO categoryRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> delete(CategoryRequestDTO categoryRequest, Locale locale);
}
