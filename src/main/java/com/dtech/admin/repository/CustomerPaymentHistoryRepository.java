package com.dtech.admin.repository;

import com.dtech.admin.model.CustomerPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPaymentHistoryRepository extends JpaRepository<CustomerPaymentHistory, Long>, JpaSpecificationExecutor<CustomerPaymentHistory> {
}
