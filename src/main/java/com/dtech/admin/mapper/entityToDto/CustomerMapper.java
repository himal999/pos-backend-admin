package com.dtech.admin.mapper.entityToDto;

import com.dtech.admin.dto.response.BillingItemResponseDTO;
import com.dtech.admin.dto.response.BillingResponseDTO;
import com.dtech.admin.dto.response.CustomerPaymentHistoryResponseDTO;
import com.dtech.admin.dto.response.CustomerResponseDTO;
import com.dtech.admin.enums.*;
import com.dtech.admin.model.Billing;
import com.dtech.admin.model.BillingDetail;
import com.dtech.admin.model.Customer;
import com.dtech.admin.model.CustomerPaymentHistory;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
public class CustomerMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CustomerResponseDTO mapCustomerMapper(Customer customer) {
        try {
            log.info("mapHistoryMapper mapper {} ", customer);
            CustomerResponseDTO customerResponseDTO = modelMapper.map(customer, CustomerResponseDTO.class);
            customerResponseDTO.setTitleDescription(Title.valueOf(customerResponseDTO.getTitle()).getDescription());
            customerResponseDTO.setStatusDescription(Status.valueOf(customerResponseDTO.getStatus()).getDescription());
            return customerResponseDTO;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public static BillingResponseDTO mapBillingMapper(Billing billing) {
        try {
            log.info("mapBillingMapper mapper {} ", billing);
            BillingResponseDTO billingResponseDTO = modelMapper.map(billing, BillingResponseDTO.class);
            billingResponseDTO.setPaymentTypeDescription(PaymentType.valueOf(billingResponseDTO.getPaymentType()).getDescription());
            billingResponseDTO.setSalesTypeDescription(SalesType.valueOf(billingResponseDTO.getSalesType()).getDescription());
            billingResponseDTO.setPaymentCategoryDescription(PaymentCategory.valueOf(billingResponseDTO.getPaymentCategory()).getDescription());
            return billingResponseDTO;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public static BillingItemResponseDTO mapBillingItemDetailsMapper(BillingDetail billingDetail) {
        try {
            log.info("mapBillingItemDetailsMapper mapper {} ", billingDetail);
            BillingItemResponseDTO billingResponseDTO = modelMapper.map(billingDetail, BillingItemResponseDTO.class);
            billingResponseDTO.setItemName(billingDetail.getStock().getItem().getDescription());
            return billingResponseDTO;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public static CustomerPaymentHistoryResponseDTO mapCustomerPaymentMapper(CustomerPaymentHistory customerPaymentHistory) {
        try {
            log.info("mapCustomerPaymentMapper mapper {} ", customerPaymentHistory);
            CustomerPaymentHistoryResponseDTO customerPaymentHistoryResponseDTO = modelMapper.map(customerPaymentHistory, CustomerPaymentHistoryResponseDTO.class);
            customerPaymentHistoryResponseDTO.setPaymentTypeDescription(PaymentType.valueOf(customerPaymentHistory.getPaymentType().name()).getDescription());
            customerPaymentHistoryResponseDTO.setSettlementTypeDescription(CustomerSettlementType.valueOf(customerPaymentHistory.getSettlementType().name()).getDescription());
            return customerPaymentHistoryResponseDTO;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
