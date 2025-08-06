package com.dtech.admin.repository;
import com.dtech.admin.model.CashInOut;
import com.dtech.admin.model.CashierUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CashInOutRepository extends JpaRepository<CashInOut, Long> {
    List<CashInOut> findByCashierUserAndCreatedDateBetween(CashierUser cashierUser, Date from, Date to);
}
