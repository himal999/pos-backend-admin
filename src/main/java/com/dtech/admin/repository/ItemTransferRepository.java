package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.ItemTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemTransferRepository extends JpaRepository<ItemTransfer, Long> , JpaSpecificationExecutor<ItemTransfer> {

    Optional<ItemTransfer> findByIdAndStatusNot(Long id, Status status);
}
