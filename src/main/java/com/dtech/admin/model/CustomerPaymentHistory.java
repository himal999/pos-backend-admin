package com.dtech.admin.model;

import com.dtech.admin.enums.CustomerSettlementType;
import com.dtech.admin.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_payment_history")
@Data
public class CustomerPaymentHistory extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "payment_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "invoice_number",nullable = false,updatable = false,unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_code",referencedColumnName = "code")
    private Location location;

    @Column(name = "paid_amount",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal paidAmount;

    @Column(name = "balance_amount",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal balanceAmount;

    @Column(name = "remark")
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_user_id",referencedColumnName = "id")
    private CashierUser cashierUser;

    @Column(name = "settlement_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerSettlementType settlementType;

    @Column(name = "after_payment_pending_balance",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal afterPaymentPendingBalance;

}