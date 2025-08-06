package com.dtech.admin.repository;

import com.dtech.admin.model.GRN;
import com.dtech.admin.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GRNRepository extends JpaRepository<GRN, Long>, JpaSpecificationExecutor<GRN> {

    Optional<GRN> findByLocation(Location location);
}
