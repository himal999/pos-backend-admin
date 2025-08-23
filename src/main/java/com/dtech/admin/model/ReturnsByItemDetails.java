package com.dtech.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "returns_by_item_details")
@Data
public class ReturnsByItemDetails extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "code",nullable = false)
    private String code;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "lable_price",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal lablePrice;

    @Column(name = "item_cost",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal itemCost;

    @Column(name = "retail_price",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal retailPrice;

    @Column(name = "wholesale_price",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal wholesalePrice;

    @Column(name = "retail_discount")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal retailDiscount;

    @Column(name = "wholesale_discount")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal wholesaleDiscount;

    @Column(name = "sales_price")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private BigDecimal salesPrice;

    @Column(name = "sales_disscount")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "0.00")
    private Double salesDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id",referencedColumnName = "id")
    private Stock stock;
}
