package com.dtech.admin.repository;

import com.dtech.admin.model.GRN;
import com.dtech.admin.model.Location;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface GRNRepository extends JpaRepository<GRN, Long>, JpaSpecificationExecutor<GRN> {

    Optional<GRN> findByLocation(Location location);
    List<GRN> findAllBySupplierId(Long supplierId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE GRN g SET g.paidAmount = :paidAmount, g.balance = :balance, g.lastModifiedBy = :lastModifiedBy, g.lastModifiedDate = CURRENT_TIMESTAMP WHERE g.id = :id")
    void updatePaymentFields(@Param("id") Long id,
                             @Param("paidAmount") BigDecimal paidAmount,
                             @Param("balance") BigDecimal balance,
                             @Param("lastModifiedBy") String lastModifiedBy);
}
