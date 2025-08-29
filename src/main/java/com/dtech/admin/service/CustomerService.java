package com.dtech.admin.service;

import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.CustomerRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.ApiResponse;
import com.dtech.admin.dto.search.CustomerSearchDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface CustomerService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> billingHistory(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> billingItemDetails(CustomerRequestDTO customerRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> settlePaymentHistory(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale);
    ResponseEntity<ApiResponse<Object>> basicEditInfo(CustomerRequestDTO customerRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> creditLimitUpdate(CustomerRequestDTO customerRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> customerPermanentRemove(CustomerRequestDTO customerRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> customerCashCreditBillingUpdate(CustomerRequestDTO customerRequestDTO, Locale locale);
}
