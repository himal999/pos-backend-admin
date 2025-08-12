package com.dtech.admin.service.impl;

import com.dtech.admin.dto.PagingResult;
import com.dtech.admin.dto.SimpleBaseDTO;
import com.dtech.admin.dto.request.ChannelRequestDTO;
import com.dtech.admin.dto.request.PaginationRequest;
import com.dtech.admin.dto.request.SupplierPaymentRequestDTO;
import com.dtech.admin.dto.request.SupplierRequestDTO;
import com.dtech.admin.dto.response.*;
import com.dtech.admin.dto.search.SupplierSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import com.dtech.admin.enums.WebPage;
import com.dtech.admin.mapper.entityToDto.SupplierMapper;
import com.dtech.admin.model.GRN;
import com.dtech.admin.model.Location;
import com.dtech.admin.model.Supplier;
import com.dtech.admin.model.SupplierPayment;
import com.dtech.admin.repository.GRNRepository;
import com.dtech.admin.repository.SupplierPaymentRepository;
import com.dtech.admin.repository.SupplierRepository;
import com.dtech.admin.service.SupplierService;
import com.dtech.admin.specifications.SupplierSpecification;
import com.dtech.admin.util.CommonPrivilegeGetter;
import com.dtech.admin.util.PaginationUtil;
import com.dtech.admin.util.ResponseMessageUtil;
import com.dtech.admin.util.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import org.hibernate.Hibernate;

@Service
@Log4j2
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private final CommonPrivilegeGetter commonPrivilegeGetter;

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final ResponseUtil responseUtil;

    @Autowired
    private final SupplierRepository supplierRepository;

    @Autowired
    private final GRNRepository grnRepository;

    @Autowired
    private final SupplierPaymentRepository supplierPaymentRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> getReferenceDate(ChannelRequestDTO channelRequestDTO, Locale locale) {
        try {
            log.info("Supplier page reference data {} ", channelRequestDTO);
            Map<String,Object> responseMap = new HashMap<>();

            AuthorizationTaskResponseDTO privileges = commonPrivilegeGetter.
                    getPrivileges(channelRequestDTO.getUsername(), WebPage.SPMP.name());

            List<SimpleBaseDTO> defaultStatus = Arrays.stream(Status.values())
                    .filter(status -> !Status.DELETE.name().equals(status.name())).map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            List<SimpleBaseDTO> title = Arrays.stream(Title.values()).
                    map(st -> new SimpleBaseDTO(st.name(), st.getDescription())).toList();

            responseMap.put("privileges", privileges);
            responseMap.put("defaultStatus", defaultStatus);
            responseMap.put("title", title);
            return ResponseEntity.ok().body(responseUtil.success(responseMap, messageSource.getMessage(ResponseMessageUtil.REFERENCE_DATA_RETRIEVED_SUCCESS, new Object[]{WebPage.SPMP.name()}, locale)));

        }catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> filterList(PaginationRequest<SupplierSearchDTO> paginationRequest, Locale locale) {
        try {
            log.info("Supplier filter list {}", paginationRequest);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest);

            Page<Supplier> suppliers = Objects.nonNull(paginationRequest.getSearch()) ?
                    supplierRepository.findAll(SupplierSpecification.getSpecification(paginationRequest.getSearch()), pageable) :
                    supplierRepository.findAll(SupplierSpecification.getSpecification(), pageable);
            log.info("Supplier filter records {}", suppliers);
            long totalElements = Objects.nonNull(paginationRequest.getSearch()) ?
                    supplierRepository.count(SupplierSpecification.getSpecification(paginationRequest.getSearch())) :
                    supplierRepository.count(SupplierSpecification.getSpecification());
            log.info("Supplier filter records map start");
            List<SupplierResponseDTO> responseDTOList = suppliers.stream()
                    .map(SupplierMapper::mapSupplierMapper).toList();
            log.info("Supplier filter records map finish");
            return ResponseEntity.ok().body(responseUtil.success((Object) new PagingResult<SupplierResponseDTO>(responseDTOList, responseDTOList.size(), totalElements),
                    messageSource.getMessage(ResponseMessageUtil.SUPPLIER_FILTER_LIST_SUCCESS,
                            null, locale)));
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> add(SupplierRequestDTO supplierRequestDTO, Locale locale) {
        try {
            log.info("Supplier add {}", supplierRequestDTO);

            Supplier supplier = com.dtech.admin.mapper.dtoToEntity.SupplierMapper.mapSupplier(supplierRequestDTO);
            supplierRepository.saveAndFlush(supplier);
            log.info("Supplier added {}", supplier);

            return ResponseEntity.ok().body(
                    responseUtil.success(
                            null,
                            messageSource.getMessage(
                                    ResponseMessageUtil.SUPPLIER_ADDED_SUCCESS,
                                    new Object[]{supplierRequestDTO.getFirstName() +" "+supplierRequestDTO.getLastName()},
                                    locale
                            )
                    )
            );

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> view(SupplierRequestDTO supplierRequestDTO, Locale locale) {
        try {
            log.info("Supplier view {}", supplierRequestDTO);
            return supplierRepository.findByIdAndStatusNot(supplierRequestDTO.getId(),Status.DELETE).map(supplier -> {
                SupplierResponseDTO supplierResponseDTO = SupplierMapper.mapSupplierMapper(supplier);
                return ResponseEntity.ok().body(responseUtil.success((Object) supplierResponseDTO, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_FOUND_BY_ID, new Object[]{supplierRequestDTO.getId()}, locale)));
            }).orElseGet(() -> {
                log.info("Supplier view not found {}", supplierRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1017, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID, new Object[]{supplierRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> update(SupplierRequestDTO supplierRequestDTO, Locale locale) {
        try {
            log.info("Supplier update {}", supplierRequestDTO);
            return supplierRepository.findByIdAndStatusNot(supplierRequestDTO.getId(),Status.DELETE).map(supplier -> {

                String newModel = new StringBuilder()
                        .append(supplierRequestDTO.getTitle())
                        .append("|")
                        .append(supplierRequestDTO.getFirstName())
                        .append("|")
                        .append(supplierRequestDTO.getLastName())
                        .append("|")
                        .append(supplierRequestDTO.getPrimaryEmail())
                        .append("|")
                        .append(supplierRequestDTO.getPrimaryMobile())
                        .append("|")
                        .append(supplierRequestDTO.getCompany())
                        .append("|")
                        .append(supplierRequestDTO.getStatus()).toString();

                String oldModel = new StringBuilder()
                        .append(supplier.getTitle().name())
                        .append("|")
                        .append(supplier.getFirstName())
                        .append("|")
                        .append(supplier.getLastName())
                        .append("|")
                        .append(supplier.getPrimaryEmail())
                        .append("|")
                        .append(supplier.getPrimaryMobile())
                        .append("|")
                        .append(supplier.getCompany())
                        .append("|")
                        .append(supplier.getStatus().name()).toString();

                if (oldModel.equals(newModel)) {
                    log.info("Supplier update status not changed to {}", newModel);
                    return ResponseEntity.ok().body(responseUtil.error(null, 1016, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_VALUES_NOT_CHANGING, null, locale)));
                }

                supplier.setStatus(Status.valueOf(supplierRequestDTO.getStatus()));
                supplier.setTitle(Title.valueOf(supplierRequestDTO.getTitle()));
                supplier.setFirstName(supplierRequestDTO.getFirstName());
                supplier.setLastName(supplierRequestDTO.getLastName());
                supplier.setCompany(supplierRequestDTO.getCompany());
                supplier.setPrimaryEmail(supplierRequestDTO.getPrimaryEmail());
                supplier.setPrimaryMobile(supplierRequestDTO.getPrimaryMobile());
                supplierRepository.saveAndFlush(supplier);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_UPDATE_SUCCESS,new Object[]{supplier.getFirstName() +" "+supplier.getLastName()}, locale)));
            }).orElseGet(() -> {
                log.info("Supplier update not found {}", supplierRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1017, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID, new Object[]{supplierRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<ApiResponse<Object>> delete(SupplierRequestDTO supplierRequestDTO, Locale locale) {
        try {
            log.info("Supplier delete {}", supplierRequestDTO);

            return supplierRepository.findByIdAndStatusNot(supplierRequestDTO.getId(),Status.DELETE).map(supplier -> {
                supplier.setStatus(Status.DELETE);
                supplierRepository.saveAndFlush(supplier);
                return ResponseEntity.ok().body(responseUtil.success(null, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_DELETE_SUCCESS,new Object[]{supplier.getFirstName() +" "+supplier.getLastName()}, locale)));
            }).orElseGet(() -> {
                log.info("Supplier delete not found {}", supplierRequestDTO.getId());
                return ResponseEntity.ok().body(responseUtil.error(null, 1017, messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID, new Object[]{supplierRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.info(e);
            throw e;
        }
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<? extends ApiResponse<?>> addPayment(SupplierPaymentRequestDTO paymentRequestDTO, Locale locale) {
        try {
            log.info("Supplier payment add {}", paymentRequestDTO);

            // Validate supplier
            Supplier supplier = supplierRepository.findByIdAndStatus(paymentRequestDTO.getSupplierId(), Status.ACTIVE)
                    .orElse(null);
            if (supplier == null) {
                return ResponseEntity.ok().body(responseUtil.error(Collections.emptyList(), 1017,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID,
                                new Object[]{paymentRequestDTO.getSupplierId()}, locale)));
            }

            // Validate GRN
            GRN grn = grnRepository.findById(paymentRequestDTO.getGrnId()).orElse(null);
            if (grn == null) {
                return ResponseEntity.ok().body(responseUtil.error(Collections.emptyList(), 1029,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_GRN_NOT_FOUND_BY_ID,
                                new Object[]{paymentRequestDTO.getGrnId()}, locale)));
            }

            // Ensure GRN belongs to supplier
            if (grn.getSupplier() == null || !grn.getSupplier().getId().equals(paymentRequestDTO.getSupplierId())) {
                return ResponseEntity.ok().body(responseUtil.error(Collections.emptyList(), 1033,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_GRN_NOT_BELONG_SUPPLIER,
                                new Object[]{paymentRequestDTO.getGrnId(), paymentRequestDTO.getSupplierId()}, locale)));
            }

            // Validate payment amount
            BigDecimal newPaidAmount = grn.getPaidAmount().add(paymentRequestDTO.getPaymentAmount());
            if (newPaidAmount.compareTo(grn.getCost()) > 0) {
                return ResponseEntity.ok().body(responseUtil.error(Collections.emptyList(), 1034,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_PAYMENT_EXCEEDS_GRN_COST,
                                new Object[]{paymentRequestDTO.getGrnId()}, locale)));
            }

            // Save payment record
            SupplierPayment payment = new SupplierPayment();
            payment.setSupplier(supplier);
            payment.setGrn(grn);
            payment.setPaymentAmount(paymentRequestDTO.getPaymentAmount());
            payment.setPaymentDate(new Date());
            payment.setCreatedBy(paymentRequestDTO.getUsername());
            payment.setLastModifiedBy(paymentRequestDTO.getUsername());
            supplierPaymentRepository.saveAndFlush(payment);

            // Partial update for GRN (no touching location_code)
            grnRepository.updatePaymentFields(grn.getId(),
                    newPaidAmount,
                    grn.getCost().subtract(newPaidAmount),
                    paymentRequestDTO.getUsername());

            // Prepare response
            SupplierPaymentResponseDTO responseDTO = modelMapper.map(payment, SupplierPaymentResponseDTO.class);
            responseDTO.setSupplierId(supplier.getId());
            responseDTO.setGrnId(grn.getId());

            return ResponseEntity.ok().body(responseUtil.success(responseDTO,
                    messageSource.getMessage(ResponseMessageUtil.SUPPLIER_PAYMENT_ADDED_SUCCESS,
                            new Object[]{supplier.getFirstName() + " " + supplier.getLastName(), paymentRequestDTO.getGrnId()}, locale)));

        } catch (Exception e) {
            log.error("Error processing supplier payment", e);
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = false)
    public ResponseEntity<? extends ApiResponse<? extends Object>> getSupplierBalance(SupplierRequestDTO supplierRequestDTO, Locale locale) {
        try {
            log.info("Supplier balance request {}", supplierRequestDTO);
            return supplierRepository.findByIdAndStatusNot(supplierRequestDTO.getId(), Status.DELETE).map(supplier -> {
                List<GRN> grns = grnRepository.findAllBySupplierId(supplier.getId());
                List<SupplierPayment> payments = supplierPaymentRepository.findBySupplierId(supplier.getId());

                SupplierBalanceResponseDTO balanceResponse = new SupplierBalanceResponseDTO();
                balanceResponse.setSupplierId(supplier.getId());
                balanceResponse.setSupplierName(supplier.getFirstName() + " " + supplier.getLastName());

                List<GRNBalanceDTO> grnBalances = new ArrayList<>();
                for (GRN grn : grns) {
                    GRNBalanceDTO grnBalance = new GRNBalanceDTO();
                    grnBalance.setGrnId(grn.getId());
                    grnBalance.setCost(grn.getCost());
                    grnBalance.setPaidAmount(grn.getPaidAmount());
                    grnBalance.setBalance(grn.getBalance());

                    List<SupplierPaymentResponseDTO> paymentHistory = payments.stream()
                            .filter(payment -> payment.getGrn().getId().equals(grn.getId()))
                            .map(payment -> {
                                SupplierPaymentResponseDTO dto = modelMapper.map(payment, SupplierPaymentResponseDTO.class);
                                dto.setSupplierId(supplier.getId());
                                dto.setGrnId(grn.getId());
                                return dto;
                            }).toList();
                    grnBalance.setPaymentHistory(paymentHistory);
                    grnBalances.add(grnBalance);
                }

                balanceResponse.setGrnBalances(grnBalances);
                return ResponseEntity.<ApiResponse<SupplierBalanceResponseDTO>>ok().body(responseUtil.success(balanceResponse,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_BALANCE_RETRIEVED_SUCCESS,
                                new Object[]{supplier.getFirstName() + " " + supplier.getLastName()}, locale)));
            }).orElseGet(() -> {
                log.info("Supplier not found {}", supplierRequestDTO.getId());
                return ResponseEntity.<ApiResponse<Object>>ok().body(responseUtil.error(null, 1017,
                        messageSource.getMessage(ResponseMessageUtil.SUPPLIER_NOT_FOUND_BY_ID,
                                new Object[]{supplierRequestDTO.getId()}, locale)));
            });
        } catch (Exception e) {
            log.error("Error retrieving supplier balance", e);
            throw e;
        }
    }}
