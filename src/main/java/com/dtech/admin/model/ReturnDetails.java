package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "returns_details")
@Data
public class ReturnDetails extends AdminAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "returns_id",referencedColumnName = "id")
    private Returns returns;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_details_id",referencedColumnName = "id",nullable = false)
    private BillingDetail billingDetail;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "returns_by_item_id",referencedColumnName = "id")
    private ReturnsByItemDetails returnsByItemDetails;

    @Column(name = "qty",nullable = false)
    private BigDecimal qty;

}
