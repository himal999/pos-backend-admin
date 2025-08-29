package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.CustomerRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.dto.search.CustomerSearchDTO;
import com.dtech.admin.enums.PaymentCategory;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.CustomerMapper;
import com.dtech.admin.model.Billing;
import com.dtech.admin.model.Customer;
import com.dtech.admin.model.CustomerPaymentHistory;
import com.dtech.admin.repository.BillingRepository;
import com.dtech.admin.repository.CustomerPaymentHistoryRepository;
import com.dtech.admin.repository.CustomerRepository;
import com.dtech.admin.service.CustomerService;
import com.dtech.admin.specifications.BillingSpecification;
import com.dtech.admin.specifications.CustomerPaymentHistorySpecification;
import com.dtech.admin.specifications.CustomerSpecification;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.PaginationUtil;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final BillingRepository billingRepository;

    @Autowired
    private final CustomerPaymentHistoryRepository customerPaymentHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Stock page reference data {} ", channelRequestDTO);
            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.CUSM.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st ->
                            new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);

            return ResponseEntity.ok().body(responseUtil.success(responseMap,
                    messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS,
                            new Object[]{WebPage.CUSM.name()}, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        try {

            log.info("Filter list data customer {} ", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Customer> customers = Objects.nonNull(paginationRequest.getSearch()) ?
                    customerRepository.findAll(CustomerSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    customerRepository.findAll(CustomerSpecification.getSpecification(), pageable);
            log.info("Customer filter records {}", customers);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    customerRepository.count(CustomerSpecification.getSpecification(paginationRequest.getSearch())) :
                    customerRepository.count(CustomerSpecification.getSpecification());
            log.info("Customer filter records map start");
            List<CustomerResponseDTO> responseDTOList = customers.stream()
                    .map(CustomerMapper::mapCustomerMapper).toList();
            log.info("Customer filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<CustomerResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.CUSTOMER_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> billingHistory(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        try {

            log.info("Customer view data customer {} ", paginationRequest);
            return customerRepository.findByIdAndStatusNot(paginationRequest.getId(), Status.DELETE).map(cus -> {

                Pageable pageable = PaginationUtil.getPageable(paginationRequest);
                Page<Billing> billings = Objects.nonNull(paginationRequest.getSearch()) ?
                        billingRepository.findAll(BillingSpecification.getSpecification(paginationRequest.getSearch(), cus), pageable) :
                        billingRepository.findAll(BillingSpecification.getSpecification(cus), pageable);
                log.info("Billing filter records {}", billings);
                long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                        billingRepository.count(BillingSpecification.getSpecification(paginationRequest.getSearch(), cus)) :
                        billingRepository.count(BillingSpecification.getSpecification(cus));

                List<BillingResponseDTO> billingResponseDTOS = billings.stream()
                        .map(CustomerMapper::mapBillingMapper).toList();
                log.info("Billing filter records map finish");

                billingResponseDTOS.forEach(billingResponse -> {
                    if (billingResponse.getPaymentCategory().equals(PaymentCategory.CREDIT.name())) {
                        billingResponse.setPendingBalance(billingResponse.getTotalAmount()
                                .subtract(billingResponse.getActualPayAmount()));
                    } else {
                        billingResponse.setPendingBalance(BigDecimal.ZERO);
                    }
                });

                return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<BillingResponseDTO>(billingResponseDTOS, billingResponseDTOS.size(), totalElements),
                        messageSource.getMessage(ResponseMessageUtil.CUSTOMER_BILLING_FILTER_LIST_SUCCESS,
                                null, locale)));

            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", paginationRequest.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{paginationRequest.getId()},
                                        locale
                                )
                        )
                );
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> billingItemDetails(CustomerRequestDTO customerRequestDTO, Locale locale) {
        try {

            log.info("Customer billing item details view data customer {} ", customerRequestDTO);
            return billingRepository.findByInvoiceNumber(customerRequestDTO.getInvoiceNumber()).map(billing -> {

                List<BillingItemResponseDTO> billingItemResponseDTOS = billing.getBillingDetailList().stream()
                        .map(CustomerMapper::mapBillingItemDetailsMapper).toList();
                log.info("Billing item details map success {}", billingItemResponseDTOS);
                return ResponseEntity.ok().body(
                        responseUtil.success((Object)
                                        billingItemResponseDTOS,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_BILLING_ITEM_FOUND_BY_INVOICE_NUMBER,
                                        null,
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("Customer billing item not found data customer {} ", customerRequestDTO.getInvoiceNumber());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1045,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_BILLING_ITEM_NOT_FOUND_BY_ID,
                                        new Object[]{customerRequestDTO.getId()},
                                        locale
                                )
                        )
                );
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> settlePaymentHistory(PaginationRequest<CustomerSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Customer settlement payment history data customer {} ", paginationRequest);

            return customerRepository.findByIdAndStatusNot(paginationRequest.getId(), Status.DELETE).map(cus -> {

                Pageable pageable = PaginationUtil.getPageable(paginationRequest);
                Page<CustomerPaymentHistory> customerPaymentHistories = Objects.nonNull(paginationRequest.getSearch()) ?
                        customerPaymentHistoryRepository.findAll(CustomerPaymentHistorySpecification.getSpecification(paginationRequest.getSearch(), cus), pageable) :
                        customerPaymentHistoryRepository.findAll(CustomerPaymentHistorySpecification.getSpecification(cus), pageable);

                log.info("Customer payment history filter records {}", customerPaymentHistories);
                long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                        customerPaymentHistoryRepository.count(CustomerPaymentHistorySpecification.getSpecification(paginationRequest.getSearch(), cus)) :
                        customerPaymentHistoryRepository.count(CustomerPaymentHistorySpecification.getSpecification(cus));

                List<CustomerPaymentHistoryResponseDTO> customerPaymentHistoryResponseDTOS = customerPaymentHistories.stream()
                        .map(CustomerMapper::mapCustomerPaymentMapper).toList();
                log.info("Customer payment history filter records map finish");

                return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<CustomerPaymentHistoryResponseDTO>(customerPaymentHistoryResponseDTOS, customerPaymentHistoryResponseDTOS.size(), totalElements),
                        messageSource.getMessage(ResponseMessageUtil.CUSTOMER_PAYMENT_HISTORY_FILTER_LIST_SUCCESS,
                                null, locale)));

            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", paginationRequest.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{paginationRequest.getId()},
                                        locale
                                )
                        )
                );
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> basicEditInfo(CustomerRequestDTO customerRequestDTO, Locale locale) {
        try {
            log.info("Customer basic edit info data customer {} ", customerRequestDTO);

            return customerRepository.findByIdAndStatusNot(customerRequestDTO.getId(), Status.DELETE).map(cus -> {

                String newModel = new StringBuilder()
                        .append(customerRequestDTO.getTitle())
                        .append("|")
                        .append(customerRequestDTO.getFirstName())
                        .append("|")
                        .append(customerRequestDTO.getLastName())
                        .append("|")
                        .append(customerRequestDTO.getCity())
                        .append("|")
                        .append(customerRequestDTO.getMobile())
                        .append("|")
                        .append(customerRequestDTO.getEmail()).toString();

                String oldModel = new StringBuilder()
                        .append(cus.getTitle().name())
                        .append("|")
                        .append(cus.getFirstName())
                        .append("|")
                        .append(cus.getLastName())
                        .append("|")
                        .append(cus.getCity())
                        .append("|")
                        .append(cus.getMobile())
                        .append("|")
                        .append(cus.getEmail()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Customer details not found {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1046, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_DETAILS_NOT_CHAINING, null, locale)));
                }

                if (!customerRequestDTO.getMobile().equals(cus.getMobile())) {
                    log.info("Customer mobile number changing not found {}", cus.getMobile());
                    if (customerRepository.existsAllByMobileAndStatusNot(customerRequestDTO.getMobile(), Status.DELETE)) {
                        log.info("Mobile number already exist {}", cus.getMobile());
                        return ResponseEntity.ok().body(responseUtil.error(null, 1047, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_MOBILE_NUMBER_ALREADY_EXISTS, new Object[]{customerRequestDTO.getMobile()}, locale)));
                    }
                }
                log.info("Customer details mapper start");
                com.dtech.admin.mapper.dtoToEntity.CustomerMapper.mapCustomerMapper(customerRequestDTO, cus);
                log.info("Customer edit info data customer {} ", customerRequestDTO);

                customerRepository.saveAndFlush(cus);

                return ResponseEntity.ok().body(
                        responseUtil.success(null,
                                messageSource.getMessage(ResponseMessageUtil.CUSTOMER_DETAILS_UPDATE_SUCCESS,
                                        new Object[]{cus.getFirstName() + " " + cus.getLastName()}, locale)));


            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", customerRequestDTO.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{customerRequestDTO.getId()},
                                        locale
                                )
                        )
                );
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> creditLimitUpdate(CustomerRequestDTO customerRequestDTO, Locale locale) {
        try {
            log.info("Customer credit limit update data customer {} ", customerRequestDTO.getCreditLimit());

            return customerRepository.findByIdAndStatusNot(customerRequestDTO.getId(), Status.DELETE).map(cus -> {

                if (customerRequestDTO.getCreditLimit().equals(cus.getCreditLimit())) {
                    log.info("Customer credit limit changing not found {}", cus.getCreditLimit());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1048, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_CREDIT_LIMIT_NOT_CHAINING, null, locale)));
                } else if (customerRequestDTO.getCreditLimit().compareTo(cus.getPendingBalance()) < 0) {
                    log.info("Credit limit can't be less than pending balance {}", cus.getPendingBalance());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1049, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_CREDIT_LIMIT_LESS_THAN_PENDING_BALANCE, null, locale)));

                }

                cus.setCreditLimit(customerRequestDTO.getCreditLimit());
                customerRepository.saveAndFlush(cus);

                return ResponseEntity.ok().body(
                        responseUtil.success(null,
                                messageSource.getMessage(ResponseMessageUtil.CUSTOMER_CREDIT_LIMIT_UPDATE_SUCCESS,
                                        new Object[]{cus.getFirstName() + " " + cus.getLastName()}, locale)));

            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", customerRequestDTO.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{customerRequestDTO.getId()},
                                        locale
                                )
                        )
                );
            });


        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> customerPermanentRemove(CustomerRequestDTO customerRequestDTO, Locale locale) {
        try {
            log.info("Customer permanent delete data customer {} ", customerRequestDTO.getId());

            return customerRepository.findByIdAndStatusNot(customerRequestDTO.getId(), Status.DELETE).map(cus -> {

                if (cus.getPendingBalance().compareTo(BigDecimal.ZERO) > 0) {
                    log.info("Customer pending balance greater than 0  {}", cus.getPendingBalance());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1050, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_DELETE_PENDING_BALANCE_EXISTS, null, locale)));
                }

                cus.setStatus(Status.DELETE);
                customerRepository.saveAndFlush(cus);

                return ResponseEntity.ok().body(
                        responseUtil.success(null,
                                messageSource.getMessage(ResponseMessageUtil.CUSTOMER_DELETE_SUCCESS,
                                        new Object[]{cus.getFirstName() + " " + cus.getLastName()}, locale)));

            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", customerRequestDTO.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{customerRequestDTO.getId()},
                                        locale
                                )
                        )
                );
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> customerCashCreditBillingUpdate(CustomerRequestDTO customerRequestDTO, Locale locale) {
        try {
            log.info("Customer cash credit update data customer {} ", customerRequestDTO.getId());

            return customerRepository.findByIdAndStatusNot(customerRequestDTO.getId(), Status.DELETE).map(cus -> {

                if (cus.getIsActiveCredit() && customerRequestDTO.getIsActiveCredit()) {
                    log.info("Customer cash credit update data customer {} ", customerRequestDTO.getId());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1051, messageSource.getMessage(ResponseMessageUtil.CUSTOMER_CREDIT_CASH_ALREADY_EXISTS, null, locale)));

                }
                cus.setIsActiveCredit(customerRequestDTO.getIsActiveCredit());
                customerRepository.saveAndFlush(cus);

                return ResponseEntity.ok().body(
                        responseUtil.success(null,
                                messageSource.getMessage(ResponseMessageUtil.CUSTOMER_CREDIT_CASH_UPDATE_SUCCESS,
                                        new Object[]{cus.getFirstName() + " " + cus.getLastName()}, locale)));

            }).orElseGet(() -> {
                log.info("Customer not found data customer {} ", customerRequestDTO.getId());
                return ResponseEntity.ok().body(
                        responseUtil.error(
                                null,
                                1044,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CUSTOMER_NOT_FOUND_BY_ID,
                                        new Object[]{customerRequestDTO.getId()},
                                        locale
                                )
                        )
                );
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }
}
