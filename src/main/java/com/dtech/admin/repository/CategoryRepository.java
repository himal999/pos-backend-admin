package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category> {
    boolean existsByCodeAndStatusNot(String code, Status status);
    Optional<Category> findByIdAndStatusNot(Long id, Status status);
    List<Category> findAllByStatusNot(Status status);
    Optional<Category> findByCodeAndStatusNot(String code,Status status);
    Optional<Category> findByCodeAndStatus(String code,Status status);
}
