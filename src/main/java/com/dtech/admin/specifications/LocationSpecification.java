package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.LocationSearchDTO;
import com.dtech.admin.enums.LocationType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Location;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class LocationSpecification {

    public static Specification<Location> getSpecification(LocationSearchDTO filterDto) {

        log.info("Location filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getCode() != null && !filterDto.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + filterDto.getCode().toLowerCase() + "%"));
            }

            if (filterDto.getDescription() != null && !filterDto.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterDto.getDescription().toLowerCase() + "%"));
            }

            if (filterDto.getCity() != null && !filterDto.getCity().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + filterDto.getCity().toLowerCase() + "%"));
            }

            if (filterDto.getContactNumber() != null && !filterDto.getContactNumber().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("contactNumber")), "%" + filterDto.getContactNumber().toLowerCase() + "%"));
            }

            if (filterDto.getStatus() != null &&  !filterDto.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(filterDto.getStatus())));
            }

            if (filterDto.getLocationType() != null &&  !filterDto.getLocationType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("locationType"), LocationType.valueOf(filterDto.getLocationType())));
            }

            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Location> getSpecification() {
        log.info("Location filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
