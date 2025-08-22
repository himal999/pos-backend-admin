package com.dtech.admin.model;

import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cash_in_out")
@Data
public class CashInOut extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_user_id",referencedColumnName = "id")
    private CashierUser cashierUser;

    @Column(name = "cash_in_out", nullable = false)
    @Enumerated(EnumType.STRING)
    private com.dtech.admin.enums.CashInOut cashInOut;

    @Column(name = "remark")
    private String remark;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}