package com.dtech.admin.repository;

import com.dtech.admin.model.GRNItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemGRNHistoryRepository extends JpaRepository<GRNItemHistory,Long> {
}
