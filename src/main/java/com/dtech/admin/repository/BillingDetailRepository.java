package com.dtech.admin.repository;

import com.dtech.admin.model.Billing;
import com.dtech.admin.model.BillingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Long> {
    List<BillingDetail> findByBilling(Billing billing);
} 