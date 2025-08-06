package com.dtech.admin.repository;

import com.dtech.admin.model.ItemTransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTransferDetailsRepository extends JpaRepository<ItemTransferDetails, Long> {
}
