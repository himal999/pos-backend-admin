package com.dtech.admin.model;

import com.dtech.admin.enums.PaymentCategory;
import com.dtech.admin.enums.PaymentType;
import com.dtech.admin.enums.SalesType;
import com.dtech.admin.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "billing")
@Data
public class Billing extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "invoice_number",nullable = false,updatable = false,unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_user_id",referencedColumnName = "id")
    private CashierUser cashierUser;

    @Column(name = "payment_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_code",referencedColumnName = "code")
    private Location location;

    @Column(name = "sale_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesType salesType;

    @Column(name = "total_amount",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal totalAmount;

    @Column(name = "pay_amount",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal payAmount;

    @Column(name = "actual_pay_amount",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal actualPayAmount;

    @Column(name = "payment_category",nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory;

    @Column(name = "remark",nullable = false)
    private String remark;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "billing")
    private List<BillingDetail> billingDetailList;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
