package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.GRNSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Item;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
@Log4j2
public class GRNSpecification {
    public static Specification<Item> getSpecification(GRNSearchDTO filterDto) {

        log.info("GRN filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getKeyword() != null && !filterDto.getKeyword().isEmpty()) {
                String keyword = "%" + filterDto.getKeyword().toLowerCase() + "%";

                Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), keyword);
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), keyword);

                predicates.add(criteriaBuilder.or(codePredicate, namePredicate));
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
