package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "grn")
@Data
public class GRN extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "cost",nullable = false)
    private BigDecimal cost;

    @Column(name = "debit",nullable = false)
    private BigDecimal debitAmount;

    @Column(name = "credit",nullable = false)
    private BigDecimal creditAmount;

    @Column(name = "paid_amount", nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Location.class)
    @JoinColumn(name = "location_code",referencedColumnName = "code",nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Supplier.class)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;

//    @OneToMany(mappedBy = "grn",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<ItemGRN> itemGRNS;

    @Column(name = "remark")
    private String remark;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

}
