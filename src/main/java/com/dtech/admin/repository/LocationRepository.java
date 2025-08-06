package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> , JpaSpecificationExecutor<Location> {
    boolean existsByCodeAndStatusNot(String code, Status status);
    Optional<Location> findByIdAndStatusNot(Long id, Status status);
    Optional<Location> findByCodeAndStatus(String code, Status status);
    List<Location> findAllByStatusNot(Status status);
    List<Location> findAllByStatus(Status status);
}
