package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.ItemRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.ItemSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface ItemService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<ItemSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(ItemRequestDTO itemRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(ItemRequestDTO itemRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(ItemRequestDTO itemRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> delete(ItemRequestDTO itemRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> getNextSequenceCode(ChannelRequestDTO channelRequestDTO, Locale locale);
}
