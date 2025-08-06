package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "grn_item_history")
@Data
public class GRNItemHistory extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "item_code",referencedColumnName = "code",nullable = false)
    private Item item;

    @Column(name = "lable_price",nullable = false)
    private BigDecimal lablePrice;

    @Column(name = "item_cost",nullable = false)
    private BigDecimal itemCost;

    @Column(name = "retail_price",nullable = false)
    private BigDecimal retailPrice;

    @Column(name = "wholesale_price",nullable = false)
    private BigDecimal wholesalePrice;

    @Column(name = "retail_discount")
    private Integer retailDiscount;

    @Column(name = "wholesale_discount")
    private Integer wholesaleDiscount;

    @Column(name = "qty",nullable = false)
    private BigDecimal qty;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "grn_history_id",referencedColumnName = "id",nullable = false)
    private GRNHistory grnHistory;

}
