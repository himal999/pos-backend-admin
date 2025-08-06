package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    boolean existsByCodeAndStatusNot(String code, Status status);
    Optional<Item> findByIdAndStatusNot(Long id, Status status);
    Optional<Item> findByCodeAndStatusNot(String code, Status status);
    Optional<Item> findByCodeAndStatus(String code, Status status);
    List<Item> findAllByStatusNot(Status status);
}
