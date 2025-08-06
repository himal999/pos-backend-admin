package com.dtech.admin.model;

import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Title;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "supplier")
@Data
public class Supplier  extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "title",nullable = false)
    @Enumerated(EnumType.STRING)
    private Title title;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "primary_mobile",nullable = false)
    private String primaryMobile;

    @Column(name = "primary_email")
    private String primaryEmail;

    @Column(name = "company")
    private String company;

}
