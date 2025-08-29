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
@Table(name = "customer")
@Data
public class Customer extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "title",nullable = false)
    @Enumerated(EnumType.STRING)
    private Title title;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "mobile",nullable = false)
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "credit_limit",nullable = false)
    private BigDecimal creditLimit = BigDecimal.valueOf(100000);

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "is_active_credit",nullable = false)
    private Boolean isActiveCredit = true;

    @Column(name = "pending_balance",nullable = false)
    private BigDecimal pendingBalance = BigDecimal.ZERO;

}
