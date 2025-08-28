package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.CustomerSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Customer;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
@Log4j2
public class CustomerSpecification {
    public static Specification<Customer> getSpecification(CustomerSearchDTO filterDto) {

        log.info("Customer filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getName() != null && !filterDto.getName().isEmpty()) {
                String keyword = "%" + filterDto.getName().toLowerCase() + "%";

                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), keyword);
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), keyword);

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            if (filterDto.getMobile() != null && !filterDto.getMobile().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("mobile")), "%" + filterDto.getMobile().toLowerCase() + "%"));
            }

            if (filterDto.getCity() != null && !filterDto.getCity().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + filterDto.getCity().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null &&  !filterDto.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(filterDto.getStatus())));
            }

            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Customer> getSpecification() {
        log.info("Customer filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
