package com.dtech.admin.repository;

import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Item;
import com.dtech.admin.model.Location;
import com.dtech.admin.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> , JpaSpecificationExecutor<Stock> {

    @Query("SELECT i FROM Stock i LEFT OUTER JOIN Location l ON l.code = i.location.code LEFT OUTER JOIN Item it ON it.code = i.item.code WHERE l.code = :location AND it.code = :item AND " +
            "i.lablePrice = :lablePrice AND i.itemCost = :itemCost AND " +
            "i.retailPrice = :retailPrice AND i.wholesalePrice = :wholesalePrice AND " +
            "i.retailDiscount = :retailDiscount AND i.wholesaleDiscount = :wholesaleDiscount AND i.status != :status AND l.status != 'DELETE' AND it.status != 'DELETE' ")
    Optional<Stock> findMatchingItem(
            @Param("location") String location,
            @Param("item") String item,
            @Param("lablePrice") BigDecimal lablePrice,
            @Param("itemCost") BigDecimal itemCost,
            @Param("retailPrice") BigDecimal retailPrice,
            @Param("wholesalePrice") BigDecimal wholesalePrice,
            @Param("retailDiscount") Integer retailDiscount,
            @Param("wholesaleDiscount") Integer wholesaleDiscount,
            @Param("status") Status status
    );


    List<Stock> findAllByItem_CodeAndStatusNot(String code, Status status);
    List<Stock> findAllByStatus(Status status);
    List<Stock> findAllByItemAndLocationAndStatus(Item item, Location location,Status status);
    Optional<Stock> findAllByIdAndStatusNot(Long id, Status status);
    Optional<Stock> findByIdAndStatus(Long id, Status status);
}
