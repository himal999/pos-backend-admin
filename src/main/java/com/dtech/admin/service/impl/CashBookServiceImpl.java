package com.dtech.admin.service.impl;

import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.CashierBalanceRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.enums.CashierType;
import com.dtech.admin.enums.SalesType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.model.*;
import com.dtech.admin.repository.*;
import com.dtech.admin.service.CashBookService;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.DateTimeUtil;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CashBookServiceImpl implements CashBookService {

    private final CommonPrivilegeGetter commonPrivilegeGetter;
    private final MessageSource messageSource;
    private final ResponseUtil responseUtil;
    private final LocationRepository locationRepository;
    private final CashierUserRepository cashierUserRepository;
    private final BillingRepository billingRepository;
    private final ReturnsRepository returnsRepository;
    private final CashInOutRepository cashInOutRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Entering getReferenceDate with request: {}", channelRequestDTO);

            Map<String, Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter
                    .getPrivileges(channelRequestDTO.getUsername(), com.dtech.admin.enums.WebPage.CABM.name());
            log.info("Fetched privileges: {}", privileges);

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name()))
                    .map(st -> new SimpleBaseDTO(st.name(), st.getDescription()))
                    .toList();
            log.info("Prepared defaultStatus list: {}", defaultStatus);

            List<SimpleBaseDTO> locations = locationRepository.findAllByStatusNot(Status.DELETE)
                    .stream().map(l -> new SimpleBaseDTO(l.getCode(), l.getDescription()))
                    .toList();
            log.info("Fetched locations: {}", locations);

            List<SimpleBaseDTO> cashiers = cashierUserRepository.findAllByStatusNot(Status.DELETE)
                    .stream().map(cashierUser -> new SimpleBaseDTO(String.valueOf(cashierUser.getId()), cashierUser.getUsername()))
                    .toList();
            log.info("Fetched cashiers: {}", cashiers);

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("locations", locations);
            responseMap.put("cashiers", cashiers);

            log.info("Successfully prepared reference data response.");
            return ResponseEntity.ok().body(
                    responseUtil.success(responseMap,
                            messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS,
                                    new Object[]{com.dtech.admin.enums.WebPage.CABM.name()}, locale)));

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> cashierBalance(CashierBalanceRequestDTO cashierBalanceRequestDTO, Locale locale) throws Exception {

        try {

            log.info("Received request for cashier balance: {}", cashierBalanceRequestDTO);

            List<Location> locations = locationRepository.findAllByStatus(Status.ACTIVE);
            log.info("Fetched {} active locations", locations.size());

            Map<String, Object> allLocationsResponse = new HashMap<>();

            BigDecimal grandTotalSales = BigDecimal.ZERO;
            BigDecimal grandTotalReturns = BigDecimal.ZERO;
            BigDecimal grandTotalCashIn = BigDecimal.ZERO;
            BigDecimal grandTotalCashOut = BigDecimal.ZERO;

            Date fromDate = DateTimeUtil.getStartOfDay(cashierBalanceRequestDTO.getFromDate());
            Date toDate = DateTimeUtil.getEndOfDay(cashierBalanceRequestDTO.getToDate());
            log.info("Date range: {} - {}", fromDate, toDate);

            for (Location location : locations) {
                log.info("Processing location: {}", location.getCode());

                List<CashierUser> cashierUsers = cashierUserRepository.findByLocationAndStatus(location, Status.ACTIVE);
                log.info("Found {} active cashiers in location {}", cashierUsers.size(), location.getCode());

                Map<String, Object> locationData = new HashMap<>();
                Map<String, Object> cashierMap = new HashMap<>();

                BigDecimal locationTotalSales = BigDecimal.ZERO;
                BigDecimal locationTotalReturns = BigDecimal.ZERO;
                BigDecimal locationTotalCashIn = BigDecimal.ZERO;
                BigDecimal locationTotalCashOut = BigDecimal.ZERO;

                for (CashierUser cashierUser : cashierUsers) {
                    log.info("Processing cashier: {}", cashierUser.getUsername());

                    CashBookResponseDTO cashBookResponseDTO = new CashBookResponseDTO();

                    List<Billing> billings = billingRepository.findByCashierUserAndCreatedDateBetween(cashierUser, fromDate, toDate);
                    log.info("Found {} billings for cashier {}", billings.size(), cashierUser.getUsername());

                    final BigDecimal[] totalSales = {BigDecimal.ZERO};
                    List<SalesResponseDTO> salesList = billings.stream().map(billing -> {
                        SalesResponseDTO dto = new SalesResponseDTO();
                        dto.setId(billing.getId());
                        dto.setInvoiceNumber(billing.getInvoiceNumber());
                        dto.setPaymentType(billing.getPaymentType().name());
                        dto.setPaymentTypeDescription(billing.getPaymentType().getDescription());

                        CustomerResponseDTO customer = new CustomerResponseDTO();
                        customer.setTitle(billing.getCustomer().getTitle().name());
                        customer.setTitleDescription(Title.valueOf(billing.getCustomer().getTitle().name()).getDescription());
                        customer.setFirstName(billing.getCustomer().getFirstName());
                        customer.setLastName(billing.getCustomer().getLastName());
                        customer.setCity(billing.getCustomer().getCity());
                        dto.setCustomer(customer);

                        dto.setSalesType(billing.getSalesType().name());
                        dto.setSalesTypeDescription(SalesType.valueOf(billing.getSalesType().name()).getDescription());
                        dto.setTotalAmount(billing.getTotalAmount());
                        dto.setPayAmount(billing.getPayAmount());
                        dto.setRemark(billing.getRemark());

                        totalSales[0] = totalSales[0].add(billing.getTotalAmount() != null ? billing.getTotalAmount() : BigDecimal.ZERO);
                        return dto;
                    }).toList();
                    cashBookResponseDTO.setSales(salesList);
                    log.info("Total sales for cashier {}: {}", cashierUser.getUsername(), totalSales[0]);

                    List<Returns> returns = returnsRepository.findByCashierUserAndCreatedDateBetween(cashierUser, fromDate, toDate);
                    log.info("Found {} returns for cashier {}", returns.size(), cashierUser.getUsername());

                    final BigDecimal[] totalReturns = {BigDecimal.ZERO};
                    List<SalesReturnsResponseDTO> returnsList = returns.stream().map(r -> {
                        SalesReturnsResponseDTO ret = new SalesReturnsResponseDTO();
                        ret.setId(r.getId());
                        ret.setRemark(r.getRemark());
                        ret.setInvoiceNumber(r.getReturnsInvoice());
                        ret.setDebitAmount(r.getDebitAmount());

                        CustomerResponseDTO customer = new CustomerResponseDTO();
                        customer.setTitle(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getTitle().name());
                        customer.setTitleDescription(Title.valueOf(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getTitle().name()).getDescription());
                        customer.setFirstName(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getFirstName());
                        customer.setLastName(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getLastName());
                        customer.setCity(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getCity());
                        ret.setCustomer(customer);

                        totalReturns[0] = totalReturns[0].add(r.getDebitAmount() != null ? r.getDebitAmount() : BigDecimal.ZERO);

                        return ret;
                    }).toList();
                    cashBookResponseDTO.setReturns(returnsList);
                    log.info("Total returns for cashier {}: {}", cashierUser.getUsername(), totalReturns[0]);

                    List<CashInOut> cashInOuts = cashInOutRepository.findByCashierUserAndCreatedDateBetween(cashierUser, fromDate, toDate);
                    log.info("Found {} cash in/out transactions for cashier {}", cashInOuts.size(), cashierUser.getUsername());

                    final BigDecimal[] totalCashIn = {BigDecimal.ZERO};
                    final BigDecimal[] totalCashOut = {BigDecimal.ZERO};
                    List<SalesInOutResponseDTO> inOutList = cashInOuts.stream().map(ci -> {
                        SalesInOutResponseDTO io = new SalesInOutResponseDTO();
                        io.setId(ci.getId());
                        io.setRemark(ci.getRemark());
                        io.setAmount(ci.getAmount());
                        io.setCashInOut(ci.getCashInOut().name());
                        io.setCashInOutDescription(ci.getCashInOut().getDescription());

                        if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.IN || ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OP) {
                            totalCashIn[0] = totalCashIn[0].add(ci.getAmount() != null ? BigDecimal.valueOf(ci.getAmount()) : BigDecimal.ZERO);
                        } else if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OUT) {
                            totalCashOut[0] = totalCashOut[0].add(ci.getAmount() != null ? BigDecimal.valueOf(ci.getAmount()) : BigDecimal.ZERO);
                        }

                        return io;
                    }).toList();
                    cashBookResponseDTO.setInOuts(inOutList);

                    BigDecimal balance = totalSales[0].add(totalCashIn[0]).subtract(totalReturns[0]).subtract(totalCashOut[0]);
                    cashBookResponseDTO.setTotalSales(totalSales[0]);
                    cashBookResponseDTO.setTotalReturns(totalReturns[0]);
                    cashBookResponseDTO.setTotalCashIn(totalCashIn[0]);
                    cashBookResponseDTO.setTotalCashOut(totalCashOut[0]);
                    cashBookResponseDTO.setBalance(balance) ;

                    log.info("Cashier {} summary - Sales: {}, Returns: {}, CashIn: {}, CashOut: {}, Balance: {}", cashierUser.getUsername(), totalSales[0], totalReturns[0], totalCashIn[0], totalCashOut[0], balance);

                    locationTotalSales = locationTotalSales.add(totalSales[0]);
                    locationTotalReturns = locationTotalReturns.add(totalReturns[0]);
                    locationTotalCashIn = locationTotalCashIn.add(totalCashIn[0]);
                    locationTotalCashOut = locationTotalCashOut.add(totalCashOut[0]);

                    cashierMap.put(cashierUser.getUsername(), cashBookResponseDTO);
                }

                BigDecimal locationBalance = locationTotalSales.add(locationTotalCashIn)
                        .subtract(locationTotalReturns).subtract(locationTotalCashOut);

                Map<String, Object> summaryMap = new HashMap<>();
                summaryMap.put("totalSales", locationTotalSales);
                summaryMap.put("totalReturns", locationTotalReturns);
                summaryMap.put("totalCashIn", locationTotalCashIn);
                summaryMap.put("totalCashOut", locationTotalCashOut);
                summaryMap.put("balance", locationBalance);

                log.info("Location {} summary - Sales: {}, Returns: {}, CashIn: {}, CashOut: {}, Balance: {}", location.getCode(), locationTotalSales, locationTotalReturns, locationTotalCashIn, locationTotalCashOut, locationBalance);

                grandTotalSales = grandTotalSales.add(locationTotalSales);
                grandTotalReturns = grandTotalReturns.add(locationTotalReturns);
                grandTotalCashIn = grandTotalCashIn.add(locationTotalCashIn);
                grandTotalCashOut = grandTotalCashOut.add(locationTotalCashOut);

                locationData.put("summary", summaryMap);
                locationData.put("cashiers", cashierMap);
                allLocationsResponse.put(location.getCode(), locationData);
            }

            BigDecimal grandBalance = grandTotalSales.add(grandTotalCashIn)
                    .subtract(grandTotalReturns).subtract(grandTotalCashOut);

            Map<String, Object> grandSummary = new HashMap<>();
            grandSummary.put("totalSales", grandTotalSales);
            grandSummary.put("totalReturns", grandTotalReturns);
            grandSummary.put("totalCashIn", grandTotalCashIn);
            grandSummary.put("totalCashOut", grandTotalCashOut);
            grandSummary.put("balance", grandBalance);

            log.info("Grand summary - Sales: {}, Returns: {}, CashIn: {}, CashOut: {}, Balance: {}", grandTotalSales, grandTotalReturns, grandTotalCashIn, grandTotalCashOut, grandBalance);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("locations", allLocationsResponse);
            responseBody.put("grandSummary", grandSummary);

            return ResponseEntity.ok().body(responseUtil.success((Object) responseBody, messageSource.getMessage(ResponseMessageUtil.CASHIER_BALANCE_DATA_RETRIEVED_SUCCESS,null, locale)));

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> cashierBalanceView(CashierBalanceRequestDTO cashierBalanceRequestDTO, Locale locale) throws Exception {
        try {
            log.info("Entering cashierBalanceView with request: {}", cashierBalanceRequestDTO);

            if (cashierBalanceRequestDTO.getType().equals(CashierType.SALES.name())) {
                log.info("Request type is SALES. Looking up billing for invoice number: {}", cashierBalanceRequestDTO.getInvoiceNumber());

                return billingRepository.findByInvoiceNumber(cashierBalanceRequestDTO.getInvoiceNumber()).map(mdb -> {
                    log.info("Billing found with ID: {}", mdb.getId());

                    List<BillingDetail> billingDetails = mdb.getBillingDetails();
                    log.info("Found {} billing detail(s) for invoice {}", billingDetails.size(), cashierBalanceRequestDTO.getInvoiceNumber());

                    List<SalesDetailsResponseDTO> items = billingDetails.stream().map(bd -> {
                        SalesDetailsResponseDTO dto = new SalesDetailsResponseDTO();
                        dto.setId(bd.getId());
                        dto.setQty(bd.getQty());
                        dto.setSalesPrice(bd.getSalesPrice());
                        dto.setSalesDiscount(bd.getSalesDiscount());
                        dto.setItemCost(bd.getItemCost());
                        dto.setLablePrice(bd.getLablePrice());
                        dto.setRetailPrice(bd.getRetailPrice());
                        dto.setWholesalePrice(bd.getWholesalePrice());
                        dto.setWholesaleDiscount(bd.getWholesaleDiscount());
                        dto.setRetailDiscount(bd.getRetailDiscount());
                        dto.setTotalPrice(bd.getSalesPrice().multiply(bd.getQty()));

                        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
                        itemResponseDTO.setId(bd.getStock().getItem().getId());
                        itemResponseDTO.setCode(bd.getStock().getItem().getCode());
                        itemResponseDTO.setDescription(bd.getStock().getItem().getDescription());

                        dto.setItem(itemResponseDTO);

                        return dto;
                    }).toList();

                    log.info("Prepared {} sales detail response item(s)", items.size());

                    return ResponseEntity.ok().body(
                            responseUtil.success(
                                    (Object) items,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_BALANCE_DETAILS_RETRIEVED_SUCCESS,
                                            null,
                                            locale
                                    )
                            )
                    );

                }).orElseGet(() -> {
                    log.warn("No billing found for invoice number: {}", cashierBalanceRequestDTO.getInvoiceNumber());
                    return ResponseEntity.ok().body(
                            responseUtil.error(
                                    null,
                                    1038,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_BALANCE_INVOICE_NUMBER_NOT_FOUND,
                                            new Object[]{cashierBalanceRequestDTO.getInvoiceNumber()},
                                            locale
                                    )
                            )
                    );
                });

            } else {
                log.info("Request type is RETURNS. Looking up return for invoice number: {}", cashierBalanceRequestDTO.getInvoiceNumber());

                return returnsRepository.findByReturnsInvoice(cashierBalanceRequestDTO.getInvoiceNumber()).map(mrd -> {
                    log.info("Returns found with ID: {}", mrd.getId());

                    List<ReturnDetails> returnDetails = mrd.getReturnDetails();
                    log.info("Found {} return detail(s) for invoice {}", returnDetails.size(), cashierBalanceRequestDTO.getInvoiceNumber());

                    List<SalesReturnsDetailsResponseDTO> responseList = returnDetails.stream().map(rd -> {
                        SalesReturnsDetailsResponseDTO dto = new SalesReturnsDetailsResponseDTO();
                        dto.setId(rd.getId());
                        dto.setQty(rd.getQty());
                        return dto;
                    }).toList();

                    log.info("Prepared {} return detail response item(s)", responseList.size());

                    return ResponseEntity.ok().body(
                            responseUtil.success(
                                    (Object) responseList,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_BALANCE_DETAILS_RETRIEVED_SUCCESS,
                                            null,
                                            locale
                                    )
                            )
                    );

                }).orElseGet(() -> {
                    log.warn("No returns found for invoice number: {}", cashierBalanceRequestDTO.getInvoiceNumber());
                    return ResponseEntity.ok().body(
                            responseUtil.error(
                                    null,
                                    1038,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_BALANCE_INVOICE_NUMBER_NOT_FOUND,
                                            new Object[]{cashierBalanceRequestDTO.getInvoiceNumber()},
                                            locale
                                    )
                            )
                    );
                });
            }

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }


}
