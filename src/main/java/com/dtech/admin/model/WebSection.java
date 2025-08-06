/**
 * User: Himal_J
 * Date: 4/22/2025
 * Time: 8:06 AM
 * <p>
 */

package com.dtech.admin.model;

import com.dtech.admin.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "web_section")
@Data
public class WebSection extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(name = "code",nullable = false,updatable = false,   unique = true)
    private String code;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "sortOrder",nullable = false,   unique = true)
    private String sortOrder;

}
