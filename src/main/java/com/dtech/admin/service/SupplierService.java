package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.SupplierPaymentRequestDTO;
import com.dtech.admin.dto.request.SupplierRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.response.SupplierBalanceResponseDTO;
import com.dtech.admin.dto.search.SupplierSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface SupplierService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<SupplierSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> add(SupplierRequestDTO supplierRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> view(SupplierRequestDTO supplierRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> update(SupplierRequestDTO supplierRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> delete(SupplierRequestDTO supplierRequestDTO, Locale locale);
    ResponseEntity<? extends ApiResponse<? extends Object>> addPayment(SupplierPaymentRequestDTO paymentRequestDTO, Locale locale);
    ResponseEntity<? extends ApiResponse<? extends Object>> getSupplierBalance(SupplierRequestDTO supplierRequestDTO, Locale locale);
}
