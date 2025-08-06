package com.dtech.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "item_transfer")
@Data
public class ItemTransfer extends AdminAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false,unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Location.class)
    @JoinColumn(name = "from_location_code",referencedColumnName = "code",nullable = false)
    private Location fromLocation;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Location.class)
    @JoinColumn(name = "to_location_code",referencedColumnName = "code",nullable = false)
    private Location toLocation;

    @Column(name = "sender_remark")
    private String senderRemark;

    @Column(name = "accepts")
    private String accepts;

    @Column(name = "accepted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptedDate;

    @Column(name = "received_remark")
    private String receivedRemark;

    @Column(name = "transfer_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private com.dtech.admin.enums.Transfer transferStatus;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private com.dtech.admin.enums.Status status;

    @OneToMany(mappedBy = "itemTransfer")
    private List<ItemTransferDetails> itemTransferDetails;

}
