package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.CashierUser;
import com.dtech.admin.model.Returns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnsRepository extends JpaRepository<Returns, Long> {
    List<Returns> findByCashierUserAndCreatedDateBetweenAndStatus(CashierUser cashierUser, Date from, Date to, Status status);
    Optional<Returns> findByReturnsInvoice(String invoiceNumber);
}
