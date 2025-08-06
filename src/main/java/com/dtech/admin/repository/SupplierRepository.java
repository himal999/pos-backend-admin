package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    Optional<Supplier> findByIdAndStatusNot(Long id, Status status);
    Optional<Supplier> findByIdAndStatus(Long id, Status status);
    List<Supplier> findAllByStatusNot(Status status);
}
