package com.dtech.admin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "item_sequence")
@Data
public class ItemSequence {
    @Id
    private Integer id;
    private Long nextVal;
}