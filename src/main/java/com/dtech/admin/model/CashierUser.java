package com.dtech.admin.model;

import com.dtech.admin.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "cashier_user")
@RequiredArgsConstructor
public class CashierUser extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false,updatable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "mobile",unique = true,nullable = false)
    private String mobile;

    @Column(name = "reset",nullable = false)
    private boolean reset;

    @Column(name = "user_key",nullable = false,updatable = false)
    private String userKey;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "last_password_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeDate;

    @Column(name = "last_logged_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoggedDate;

    @Column(name = "mb_last_logged_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mbLastLoggedDate;

    @Column(name = "op_last_logged_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date opLastLoggedDate;

    @Column(name = "expecting_first_time_logging",nullable = false)
    private boolean expectingFirstTimeLogging;

    @Column(name = "password_expired_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordExpiredDate;

    @Column(name = "attempt_count",nullable = false)
    private int attemptCount;

    @Column(name = "otp_attempt_count")
    private int otpAttemptCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_code", referencedColumnName = "code")
    private Location location;

}
