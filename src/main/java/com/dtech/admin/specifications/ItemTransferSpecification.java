package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.TransferSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ItemTransferSpecification {

    public static Specification<Stock> getSpecification(TransferSearchDTO filterDto) {

        log.info("Item transfer filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Stock, Item> itemJoin = root.join("item", JoinType.LEFT);
            Join<Stock, Location> locationJoin = root.join("location", JoinType.LEFT);

            if (filterDto.getCode() != null && !filterDto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("code")), "%" + filterDto.getCode().toLowerCase() + "%"));
            }

            if (filterDto.getDescription() != null && !filterDto.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("description")), "%" + filterDto.getDescription().toLowerCase() + "%"));
            }

            if (filterDto.getFromLocation() != null &&  !filterDto.getFromLocation().isEmpty()) {
                predicates.add(criteriaBuilder.equal(locationJoin.get("code"), filterDto.getFromLocation()));
            }

            predicates.add(criteriaBuilder.equal(root.get("status"), Status.ACTIVE));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Stock> getSpecification() {
        log.info("Item transfer filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), Status.ACTIVE));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
