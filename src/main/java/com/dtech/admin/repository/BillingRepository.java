package com.dtech.admin.repository;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Billing;
import com.dtech.admin.model.CashierUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>, JpaSpecificationExecutor<Billing> {
    List<Billing> findByCashierUserAndCreatedDateBetweenAndStatus(CashierUser cashierUser, Date from, Date to,Status status);
    Optional<Billing> findByInvoiceNumber(String invoiceNumber);
}