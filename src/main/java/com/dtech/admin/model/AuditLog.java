/**
 * User: Himal_J
 * Date: 4/23/2025
 * Time: 2:04 PM
 * <p>
 */

package com.dtech.admin.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "audit_log")
@Data
public class AuditLog extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @Column(columnDefinition = "json")
    private String oldValue;

    @Column(columnDefinition = "json")
    private String newValue;

    @Column(name = "ip_address",nullable = false,updatable = false)
    private String ipAddress;

    @Column(name = "user_agent",nullable = false,updatable = false)
    private String userAgent;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "task",nullable = false,updatable = false,referencedColumnName = "code")
    private WebTask task;

    @Column(name = "task_description",nullable = false,updatable = false)
    private String taskDescription;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "page",nullable = true,updatable = false,referencedColumnName = "code")
    private WebPage page;

}
