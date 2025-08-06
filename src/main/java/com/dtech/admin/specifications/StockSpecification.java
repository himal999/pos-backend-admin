package com.dtech.admin.specifications;

import com.dtech.admin.dto.search.StockSearchDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.Item;
import com.dtech.admin.model.Location;
import com.dtech.admin.model.Stock;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class StockSpecification {

    public static Specification<Stock> getSpecification(StockSearchDTO filterDto) {

        log.info("Stack filter: " + filterDto);

        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Stock, Location> locationJoin = root.join("location", JoinType.LEFT);
            Join<Stock, Item> itemJoin = root.join("item", JoinType.LEFT);

            if (filterDto.getLocation() != null && !filterDto.getLocation().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("description")), "%" + filterDto.getLocation().toLowerCase() + "%"));
            }

            if (filterDto.getItemCode() != null && !filterDto.getItemCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("code")), "%" + filterDto.getItemCode().toLowerCase() + "%"));
            }

            if (filterDto.getItemDescription() != null && !filterDto.getItemDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemJoin.get("description")), "%" + filterDto.getItemDescription().toLowerCase() + "%"));
            }

            if (filterDto.getQty() != null &&  !filterDto.getQty().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("qty"), filterDto.getQty()));
            }

            if (filterDto.getQtyOperator() != null && !filterDto.getQtyOperator().isEmpty()) {
                String qtyFilter = filterDto.getQtyOperator().trim();

                Pattern pattern = Pattern.compile("^(<=|>=|=|<|>)(\\d+)$");
                Matcher matcher = pattern.matcher(qtyFilter);

                if (matcher.find()) {
                    String operator = matcher.group(1);
                    BigDecimal qtyValue = BigDecimal.valueOf(Long.parseLong(matcher.group(2)));

                    switch (operator) {
                        case "=":
                            predicates.add(criteriaBuilder.equal(root.get("qty"), qtyValue));
                            break;
                        case ">":
                            predicates.add(criteriaBuilder.gt(root.get("qty"), qtyValue));
                            break;
                        case ">=":
                            predicates.add(criteriaBuilder.ge(root.get("qty"), qtyValue));
                            break;
                        case "<":
                            predicates.add(criteriaBuilder.lt(root.get("qty"), qtyValue));
                            break;
                        case "<=":
                            predicates.add(criteriaBuilder.le(root.get("qty"), qtyValue));
                            break;
                        default:
                            break;
                    }
                }
            }
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Stock> getSpecification() {
        log.info("Stock filter default :");
        return (root, query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("status"), Status.DELETE)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
