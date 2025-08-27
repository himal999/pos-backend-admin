package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.GRNHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GRNHistoryRepository extends JpaRepository<GRNHistory, Long> , JpaSpecificationExecutor<GRNHistory> {
    Optional<GRNHistory> findByIdAndStatusNot(Long id, Status status);
}
