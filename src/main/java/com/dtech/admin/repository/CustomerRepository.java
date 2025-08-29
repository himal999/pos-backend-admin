package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> , JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByIdAndStatusNot(Long id, Status status);
    boolean existsAllByMobileAndStatusNot(String mobile,Status status);
}
