package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> , JpaSpecificationExecutor<Brand> {
    boolean existsByCodeAndStatusNot(String code, Status status);
    Optional<Brand> findByIdAndStatusNot(Long id, Status status);
    Optional<Brand> findByCodeAndStatusNot(String code, Status status);
    Optional<Brand> findByCodeAndStatus(String code, Status status);
    List<Brand> findAllByStatusNot(Status status);
}
