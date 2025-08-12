package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "supplier_payment")
@Entity
public class SupplierPayment extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_id", referencedColumnName = "id", nullable = false)
    private GRN grn;

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "payment_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
}
