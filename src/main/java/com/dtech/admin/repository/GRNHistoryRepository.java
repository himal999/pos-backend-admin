package com.dtech.admin.repository;

import com.dtech.admin.model.GRNHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GRNHistoryRepository extends JpaRepository<GRNHistory, Long> {
}
