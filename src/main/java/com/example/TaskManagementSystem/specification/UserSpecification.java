package com.example.TaskManagementSystem.specification;

import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.entity.UserRole;
import com.example.TaskManagementSystem.form.UserFilterForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    // build dynamic user filters for admin search
    public static Specification<User> buildWhere(UserFilterForm form) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // search by username (LIKE - case insensitive)
            if(form.getUsernameSearch() != null && !form.getUsernameSearch().isEmpty()){
                String value = "%" + form.getUsernameSearch().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")), 
                    value
                ));
            }

            // search by email (LIKE)
            if(form.getEmailSearch() != null && !form.getEmailSearch().isEmpty()){
                String value = "%" + form.getEmailSearch().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(root.get("email"), value));
            }

            // search by role (exact match, case-insensitive)
            if(form.getRoleSearch() != null && !form.getRoleSearch().isEmpty()){
                String roleValue = form.getRoleSearch().trim().toUpperCase();
                try {
                    // Try to parse as enum value (ADMIN or USER)
                    UserRole role = UserRole.valueOf(roleValue);
                    // Use exact enum match
                    predicates.add(criteriaBuilder.equal(root.get("role"), role));
                } catch (IllegalArgumentException e) {
                    // If not a valid enum value, try case-insensitive string comparison
                    predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("role").as(String.class)),
                        roleValue
                    ));
                }
            }

            // id range filter
            if (form.getMinId() != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("id"), form.getMinId()));
            }

            if (form.getMaxId() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("id"), form.getMaxId()));
            }

            // combine all search with AND
            return criteriaBuilder.and(predicates.toArray((new Predicate[0])));
        };
    }
}
