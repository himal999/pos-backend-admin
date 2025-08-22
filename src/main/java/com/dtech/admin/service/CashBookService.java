package com.dtech.admin.service;

import com.dtech.admin.dto.request.CashierBalanceActionRequestDTO;
import com.dtech.admin.dto.request.CashierBalanceRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface CashBookService {
    ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale);
    ResponseEntity<ApiResponse<Object>> cashierBalance(CashierBalanceRequestDTO cashierBalanceRequestDTO, Locale locale) throws Exception;
    ResponseEntity<ApiResponse<Object>> cashierBalanceView(CashierBalanceRequestDTO cashierBalanceRequestDTO, Locale locale) throws Exception;
    ResponseEntity<ApiResponse<Object>> edit(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, Locale locale) throws Exception;
    ResponseEntity<ApiResponse<Object>> delete(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, Locale locale) throws Exception;
    ResponseEntity<ApiResponse<Object>> add(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, Locale locale) throws Exception;
}
