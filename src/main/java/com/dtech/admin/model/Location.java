package com.dtech.admin.model;

import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "location")
@Data
public class Location  extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "code",nullable = false,updatable = false)
    private String code;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "contact_number",nullable = false)
    private String contactNumber;

    @Column(name = "location_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

}
