package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "grn_history")
@Data
public class GRNHistory extends AdminAudit implements Serializable {
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id",referencedColumnName = "id",nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "location_code",referencedColumnName = "code",nullable = false)
    private Location location;

    @OneToMany(mappedBy = "grnHistory")
    private List<GRNItemHistory> itemGRNS;

    @Column(name = "remark")
    private String remark;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
}
