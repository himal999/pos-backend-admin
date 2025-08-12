package com.dtech.admin.repository;

import com.dtech.admin.model.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    List<SupplierPayment> findBySupplierIdAndGrnId(Long supplierId, Long grnId);
    List<SupplierPayment> findBySupplierId(Long supplierId);
}
