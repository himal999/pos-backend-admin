package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.SupplierSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Supplier;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SupplierSpecification {

    public static Specification<Supplier> getSpecification(SupplierSearchDTO filterDto) {

        log.info("Supplier filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getFirstName() != null && !filterDto.getFirstName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + filterDto.getFirstName().toLowerCase() + "%"));
            }

            if (filterDto.getLastName() != null && !filterDto.getLastName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + filterDto.getLastName().toLowerCase() + "%"));
            }

            if (filterDto.getPrimaryEmail() != null &&  !filterDto.getPrimaryEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("primaryEmail")), "%" + filterDto.getPrimaryEmail().toLowerCase() + "%"));
            }

            if (filterDto.getPrimaryMobile() != null &&  !filterDto.getPrimaryMobile().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("primaryMobile")), "%" + filterDto.getPrimaryMobile().toLowerCase() + "%"));
            }

            if (filterDto.getCompany() != null &&  !filterDto.getCompany().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("company")), "%" + filterDto.getCompany().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null &&  !filterDto.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(filterDto.getStatus())));
            }

            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Supplier> getSpecification() {
        log.info("Supplier filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
