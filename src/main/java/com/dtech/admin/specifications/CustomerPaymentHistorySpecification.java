package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.CustomerSearchDTO;
import com.dtech.admin.enums.CustomerSettlementType;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Customer;
import com.dtech.admin.model.CustomerPaymentHistory;
import com.dtech.admin.model.Location;
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
public class CustomerPaymentHistorySpecification {
    public static Specification<CustomerPaymentHistory> getSpecification(CustomerSearchDTO filterDto, Customer customer) {

        log.info("Customer payment history filter: " + filterDto);

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<CustomerPaymentHistory, Location> locationJoin = root.join("location", JoinType.LEFT);

            if (filterDto.getFromDate() != null && !filterDto.getFromDate().isEmpty()) {
                try {
                    Date fromDate = DateTimeUtil.getStartOfDay(filterDto.getFromDate());
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),fromDate));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            if (filterDto.getToDate() != null && !filterDto.getToDate().isEmpty()) {
                try {
                    Date toDate = DateTimeUtil.getEndOfDay(filterDto.getToDate());
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),toDate));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            if(filterDto.getLocation() != null && !filterDto.getLocation().isEmpty()) {
                predicates.add(criteriaBuilder.equal(locationJoin.get("code"), filterDto.getLocation()));
            }

            if(filterDto.getPaymentCategory() != null && !filterDto.getPaymentCategory().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("settlementType"), CustomerSettlementType.valueOf(filterDto.getLocation())));
            }

            predicates.add(criteriaBuilder.equal(root.get("customer"),customer));
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<CustomerPaymentHistory> getSpecification(Customer customer) {
        log.info("Customer  payment history filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("customer"), customer));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

}
