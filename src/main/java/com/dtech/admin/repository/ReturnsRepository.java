package com.dtech.admin.repository;

import com.dtech.admin.model.CashierUser;
import com.dtech.admin.model.Returns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnsRepository extends JpaRepository<Returns, Long> {
    List<Returns> findByCashierUserAndCreatedDateBetween(CashierUser cashierUser, Date from, Date to);
    Optional<Returns> findByReturnsInvoice(String invoiceNumber);
}
