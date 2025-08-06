package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.ItemSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Unit;
import com.dtech.admin.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ItemSpecification {

    public static Specification<Item> getSpecification(ItemSearchDTO filterDto) {

        log.info("Item filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Item, Category> categoryJoin = root.join("category", JoinType.LEFT);
            Join<Item, Brand> brandJoin = root.join("brand", JoinType.LEFT);

            if (filterDto.getCode() != null && !filterDto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + filterDto.getCode().toLowerCase() + "%"));
            }

            if (filterDto.getDescription() != null && !filterDto.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterDto.getDescription().toLowerCase() + "%"));
            }

            if (filterDto.getCategory() != null && !filterDto.getCategory().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("code")), "%" + filterDto.getCategory().toLowerCase() + "%"));
            }

            if (filterDto.getBrand() != null && !filterDto.getBrand().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(brandJoin.get("code")), "%" + filterDto.getBrand().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null &&  !filterDto.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(filterDto.getStatus())));
            }

            if (filterDto.getUnit() != null &&  !filterDto.getUnit().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("unit"), Unit.valueOf(filterDto.getUnit())));
            }

            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Item> getSpecification() {
        log.info("Item filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
