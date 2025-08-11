package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.*;
import com.dtech.admin.util.DateTimeUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public class TransferSpecification {

    public static Specification<ItemTransfer> getSpecification(TransferSearchDTO filterDto) {

        log.info("Transfer filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<ItemTransfer, Location> fromLocation = root.join("fromLocation", JoinType.LEFT);
            Join<ItemTransfer, Location> toLocation = root.join("toLocation", JoinType.LEFT);
            Join<ItemTransfer, Item> itemJoin = root.join("itemTransferDetails", JoinType.LEFT).join("item",JoinType.LEFT);

            if (filterDto.getCode() != null && !filterDto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("code")), "%" + filterDto.getCode().toLowerCase() + "%"));
            }

            if (filterDto.getDescription() != null && !filterDto.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("description")), "%" + filterDto.getDescription().toLowerCase() + "%"));
            }

            if (filterDto.getFromLocation() != null && !filterDto.getFromLocation().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fromLocation.get("code")), "%" + filterDto.getFromLocation().toLowerCase() + "%"));
            }

            if (filterDto.getToLocation() != null && !filterDto.getToLocation().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(toLocation.get("code")), "%" + filterDto.getToLocation().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null && !filterDto.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), filterDto.getStatus()));
            }

            if (filterDto.getTransferStatus() != null && !filterDto.getTransferStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("transferStatus")), filterDto.getTransferStatus()));
            }

            if (filterDto.getFromDate() != null) {
                try {
                    log.info("Start from search");
                    Date startOfDay = DateTimeUtil.getStartOfDay(filterDto.getFromDate());
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),startOfDay));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            if (filterDto.getToDate() != null) {
                try {
                    Date endOfDay = DateTimeUtil.getEndOfDay(filterDto.getToDate());
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),endOfDay));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<ItemTransfer> getSpecification() {
        log.info("Transfer filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
