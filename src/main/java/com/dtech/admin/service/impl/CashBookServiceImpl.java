package com.dtech.admin.service.impl;

import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.CashierBalanceActionRequestDTO;
import com.dtech.admin.dto.request.CashierBalanceRequestDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.enums.*;
import com.dtech.admin.mapper.dtoToEntity.CashInOutMapper;
import com.dtech.admin.model.*;
import com.dtech.admin.model.CashInOut;
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
    private final StockRepository stockRepository;

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

                        if(billing.getCustomer() != null){
                            CustomerResponseDTO customer = new CustomerResponseDTO();
                            customer.setTitle(billing.getCustomer().getTitle().name());
                            customer.setTitleDescription(Title.valueOf(billing.getCustomer().getTitle().name()).getDescription());
                            customer.setFirstName(billing.getCustomer().getFirstName());
                            customer.setLastName(billing.getCustomer().getLastName());
                            customer.setCity(billing.getCustomer().getCity());
                            dto.setCustomer(customer);
                        }

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

                        if(r.getReturnDetails().getFirst().getBillingDetail() != null && r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer() != null){
                            CustomerResponseDTO customer = new CustomerResponseDTO();
                            customer.setTitle(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getTitle().name());
                            customer.setTitleDescription(Title.valueOf(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getTitle().name()).getDescription());
                            customer.setFirstName(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getFirstName());
                            customer.setLastName(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getLastName());
                            customer.setCity(r.getReturnDetails().getFirst().getBillingDetail().getBilling().getCustomer().getCity());
                            ret.setCustomer(customer);
                        }

                        totalReturns[0] = totalReturns[0].add(r.getDebitAmount() != null ? r.getDebitAmount() : BigDecimal.ZERO);

                        return ret;
                    }).toList();
                    cashBookResponseDTO.setReturns(returnsList);
                    log.info("Total returns for cashier {}: {}", cashierUser.getUsername(), totalReturns[0]);

                    List<CashInOut> cashInOuts = cashInOutRepository.findByCashierUserAndCreatedDateBetween(cashierUser, fromDate, toDate);
                    log.info("Found {} cash in/out transactions for cashier {}", cashInOuts.size(), cashierUser.getUsername());

                    final BigDecimal[] totalCashIn = {BigDecimal.ZERO};
                    final BigDecimal[] totalCashOut = {BigDecimal.ZERO};
                    final BigDecimal[] totalCashierBalance = {BigDecimal.ZERO};
                    final BigDecimal[] totalCashierOpeningBalance = {BigDecimal.ZERO};

                    List<SalesInOutResponseDTO> inOutList = cashInOuts.stream()
                            .filter(ci -> ci.getCashInOut() == com.dtech.admin.enums.CashInOut.IN || ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OUT)
                            .map(ci -> {
                                SalesInOutResponseDTO io = new SalesInOutResponseDTO();
                                io.setId(ci.getId());
                                io.setRemark(ci.getRemark());
                                io.setAmount(ci.getAmount());
                                io.setCashInOut(ci.getCashInOut().name());
                                io.setCashInOutDescription(ci.getCashInOut().getDescription());

                                if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.IN) {
                                    totalCashIn[0] = totalCashIn[0].add(ci.getAmount() != null ? ci.getAmount() : BigDecimal.ZERO);
                                } else if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OUT) {
                                    totalCashOut[0] = totalCashOut[0].add(ci.getAmount() != null ? ci.getAmount() : BigDecimal.ZERO);
                                }

                                return io;
                            })
                            .toList();

                    cashBookResponseDTO.setInOuts(inOutList);

                    List<SalesInOutResponseDTO> opClList = cashInOuts.stream()
                            .filter(ci -> ci.getCashInOut() == com.dtech.admin.enums.CashInOut.CL || ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OP)
                            .map(ci -> {

                                SalesInOutResponseDTO io = new SalesInOutResponseDTO();
                                io.setId(ci.getId());
                                io.setRemark(ci.getRemark());
                                io.setAmount(ci.getAmount());
                                io.setCashInOut(ci.getCashInOut().name());
                                io.setCashInOutDescription(ci.getCashInOut().getDescription());

                                if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.CL) {
                                    totalCashierBalance[0] = totalCashierBalance[0].add(ci.getAmount() != null ? ci.getAmount() : BigDecimal.ZERO);
                                } else if (ci.getCashInOut() == com.dtech.admin.enums.CashInOut.OP) {
                                    totalCashierOpeningBalance[0] = totalCashierOpeningBalance[0].add(ci.getAmount() != null ? ci.getAmount() : BigDecimal.ZERO);
                                }

                                return io;
                            })
                            .toList();

                    cashBookResponseDTO.setOpeningClosed(opClList);

                    BigDecimal balance = totalSales[0].add(totalCashIn[0]).add(totalCashierOpeningBalance[0]).subtract(totalReturns[0]).subtract(totalCashOut[0]);

                    BigDecimal balanceAmount = balance.subtract(totalCashierBalance[0]);

                    cashBookResponseDTO.setTotalSales(totalSales[0]);
                    cashBookResponseDTO.setTotalReturns(totalReturns[0]);
                    cashBookResponseDTO.setTotalCashIn(totalCashIn[0]);
                    cashBookResponseDTO.setTotalCashOut(totalCashOut[0]);

                    SalesInOutResponseDTO closingBalance = new SalesInOutResponseDTO();
                    closingBalance.setCashInOut(com.dtech.admin.enums.CashInOut.CL.name());
                    closingBalance.setCashInOutDescription(com.dtech.admin.enums.CashInOut.CL.getDescription());
                    closingBalance.setAmount(totalCashierBalance[0]);

                    SalesInOutResponseDTO openingBalance = new SalesInOutResponseDTO();
                    openingBalance.setCashInOut(com.dtech.admin.enums.CashInOut.OP.name());
                    openingBalance.setCashInOutDescription(com.dtech.admin.enums.CashInOut.OP.getDescription());
                    openingBalance.setAmount(totalCashierOpeningBalance[0]);

                    cashBookResponseDTO.setOpeningBalance(openingBalance);
                    cashBookResponseDTO.setCashierBalance(closingBalance);
                    cashBookResponseDTO.setBalance(balance);
                    cashBookResponseDTO.setBalanceAmount(balanceAmount);

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

            return ResponseEntity.ok().body(responseUtil.success((Object) responseBody, messageSource.getMessage(ResponseMessageUtil.CASHIER_BALANCE_DATA_RETRIEVED_SUCCESS, null, locale)));

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
                        dto.setItemCode(rd.getBillingDetail().getStock().getItem().getCode());
                        dto.setItemName(rd.getBillingDetail().getStock().getItem().getDescription());
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

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> edit(CashierBalanceActionRequestDTO dto, Locale locale) {
        try {
            log.info("Cashier balance request: {}", dto);

            return cashInOutRepository.findById(dto.getId()).map(cashInOut -> {

                com.dtech.admin.enums.CashInOut type = cashInOut.getCashInOut();
                boolean isInOut = EnumSet.of(com.dtech.admin.enums.CashInOut.IN, com.dtech.admin.enums.CashInOut.OUT).contains(type);
                boolean isOpCl = EnumSet.of(com.dtech.admin.enums.CashInOut.OP, com.dtech.admin.enums.CashInOut.CL).contains(type);

                if (isInOut) {
                    log.info("Cash IN/OUT edit: {}", dto);
                    if (Objects.equals(cashInOut.getAmount(), dto.getAmount()) &&
                            Objects.equals(cashInOut.getRemark(), dto.getRemark())) {
                        log.info("No changes detected for IN/OUT.");
                        return valuesNotChangedResponse(locale);
                    }
                    cashInOut.setRemark(dto.getRemark());

                } else if (isOpCl) {
                    log.info("Cash OP/CL edit: {}", dto);
                    if (Objects.equals(cashInOut.getAmount(), dto.getAmount())) {
                        log.info("No changes detected for OP/CL.");
                        return valuesNotChangedResponse(locale);
                    }
                }

                cashInOut.setAmount(dto.getAmount());
                cashInOutRepository.saveAndFlush(cashInOut);

                return ResponseEntity.ok().body(
                        responseUtil.success(null,
                                messageSource.getMessage(
                                        ResponseMessageUtil.CASHIER_IN_OUT_DETAILS_UPDATE_SUCCESS,
                                        null,
                                        locale
                                )
                        )
                );

            }).orElseGet(() -> {
                log.info("Cashier not found: {}", dto.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1039,
                        messageSource.getMessage(ResponseMessageUtil.CASHIER_IN_OUT_NOT_FOUND,
                                null, locale)));
            });

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, Locale locale) throws Exception {
        try {
            log.info("Cashier balance request {} ", cashierBalanceActionRequestDTO);

            if (cashierBalanceActionRequestDTO.getRequestType().equals(CashierBalanceRequestType.IN_OUT_OP_CL.name())) {
                log.info("In out op cl");

                return cashInOutRepository.findById(cashierBalanceActionRequestDTO.getId()).map(cashInOut -> {
                    cashInOut.setStatus(Status.DELETE);
                    cashInOutRepository.saveAndFlush(cashInOut);

                    return ResponseEntity.ok().body(
                            responseUtil.success(null,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_IN_OUT_DETAILS_DELETE_SUCCESS,
                                            null,
                                            locale
                                    )
                            )
                    );

                }).orElseGet(() -> {
                    log.info("Cashier in out not found: {}", cashierBalanceActionRequestDTO.getId());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1039,
                            messageSource.getMessage(ResponseMessageUtil.CASHIER_IN_OUT_NOT_FOUND,
                                    null, locale)));
                });

            } else if (cashierBalanceActionRequestDTO.getRequestType().equals(CashierBalanceRequestType.SALES.name())) {
                log.info("Sales");

                return billingRepository.findById(cashierBalanceActionRequestDTO.getId()).map(billing -> {
                    billing.setStatus(Status.DELETE);
                    billingRepository.saveAndFlush(billing);

                    List<Stock> updatedStock = billing.getBillingDetails().stream().map(billingDetail -> {
                        log.info("Update stock {}", billingDetail.getStock().getId());
                        billingDetail.getStock().setQty(billingDetail.getStock().getQty().add(billingDetail.getQty()));
                        return billingDetail.getStock();
                    }).toList();

                    stockRepository.saveAllAndFlush(updatedStock);
                    log.info("Update stock successfully sales {}", updatedStock.size());

                    return ResponseEntity.ok().body(
                            responseUtil.success(null,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_SALES_DETAILS_DELETE_SUCCESS,
                                            null,
                                            locale
                                    )
                            )
                    );

                }).orElseGet(() -> {
                    log.info("Billing not found: {}", cashierBalanceActionRequestDTO.getId());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1040,
                            messageSource.getMessage(ResponseMessageUtil.BILLING_NOT_FOUND,
                                    null, locale)));
                });

            } else if (cashierBalanceActionRequestDTO.getRequestType().equals(CashierBalanceRequestType.RETURNS.name())) {
                log.info("Sales returns");

                return returnsRepository.findById(cashierBalanceActionRequestDTO.getId()).map(returns -> {
                    returns.setStatus(Status.DELETE);
                    returnsRepository.saveAndFlush(returns);

                    List<Stock> updatedStock = returns.getReturnDetails().stream()
                            .map(returnDetails -> {
                                log.info("Update stock {}", returnDetails.getId());

                                if (returnDetails.getBillingDetail() != null) {
                                    log.info("Billing Detail {}", returnDetails.getBillingDetail().getId());
                                    Stock stock = returnDetails.getBillingDetail().getStock();
                                    stock.setQty(stock.getQty().add(returnDetails.getQty()));
                                    return stock;
                                } else if (returnDetails.getReturnsByItemDetails() != null) {
                                    log.info("Returns by item {}", returnDetails.getReturnsByItemDetails().getId());
                                    Stock stock = returnDetails.getReturnsByItemDetails().getStock();
                                    stock.setQty(stock.getQty().add(returnDetails.getQty()));
                                    return stock;
                                } else {
                                    log.info("No BillingDetail or ReturnsByItemDetails for return detail {}", returnDetails.getId());
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    stockRepository.saveAllAndFlush(updatedStock);
                    log.info("Update stock successfully returns {}", updatedStock.size());

                    return ResponseEntity.ok().body(
                            responseUtil.success(null,
                                    messageSource.getMessage(
                                            ResponseMessageUtil.CASHIER_RETURNS_DETAILS_DELETE_SUCCESS,
                                            null,
                                            locale
                                    )
                            )
                    );

                }).orElseGet(() -> {
                    log.info("Returns not found: {}", cashierBalanceActionRequestDTO.getId());
                    return ResponseEntity.ok().body(responseUtil.error(null, 1040,
                            messageSource.getMessage(ResponseMessageUtil.BILLING_NOT_FOUND,
                                    null, locale)));
                });

            } else {
                log.info("Invalid request type: {}", cashierBalanceActionRequestDTO.getRequestType());
                return ResponseEntity.ok().body(responseUtil.error(null, 1041,
                        messageSource.getMessage(ResponseMessageUtil.INVALID_REQUEST,
                                null, locale)));
            }

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> add(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, Locale locale) throws Exception {
        try {
           log.info("Cash in out add {}", cashierBalanceActionRequestDTO.getId());

           return cashierUserRepository.findByUsername(cashierBalanceActionRequestDTO.getCashierUser()).map(cashierUser -> {
                CashInOut cashInOut = CashInOutMapper.mapCashInout(cashierBalanceActionRequestDTO, cashierUser);
                cashInOutRepository.saveAndFlush(cashInOut);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.CASH_IN_OUT_ADDED_SUCCESSFULLY,
                        new Object[]{com.dtech.admin.enums.CashInOut.valueOf(cashierBalanceActionRequestDTO.getCashInOut()).getDescription()}
                        , locale)));
            }).orElseGet(() -> {
                log.info("cashier user not found {}", cashierBalanceActionRequestDTO.getCashierUser());
                return ResponseEntity.ok().body(responseUtil.error(null, 1013, messageSource.getMessage(ResponseMessageUtil.CASHIER_USER_NOT_FOUND, new Object[]{cashierBalanceActionRequestDTO.getCashierUser()}, locale)));
            });

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    private ResponseEntity<ApiResponse<Object>> valuesNotChangedResponse(Locale locale) {
        return ResponseEntity.ok().body(responseUtil.error(null, 1040,
                messageSource.getMessage(ResponseMessageUtil.VALUES_NOT_CHANGING,
                        null, locale)));
    }

}
