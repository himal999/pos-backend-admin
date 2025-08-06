package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.TransferSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface TransferService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<TransferSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(TransferRequestDTO transferRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> transferCancel(TransferRequestDTO transferRequestDTO, Locale locale);
}
