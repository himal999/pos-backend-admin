package com.dtech.admin.repository;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.CashierUser;
import com.dtech.admin.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashierUserRepository extends JpaRepository<CashierUser, Long> {
    List<CashierUser> findByLocationAndStatus(Location location, Status status);
    List<CashierUser> findAllByStatusNot(Status status);
}
