package com.dtech.admin.repository;

import com.dtech.admin.model.ReturnDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnsDetailRepository extends JpaRepository<ReturnDetails,Long> {
}
