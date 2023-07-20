package com.przemekbarczyk.springlibraryrestapi.utility;

import com.przemekbarczyk.springlibraryrestapi.model.Book;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationUtility {

    public Specification<User> getUserSpecification(User user) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (user != null) {
                if (user.getFirstName() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("firstName")),
                            user.getFirstName().toLowerCase()));
                }
                if (user.getLastName() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("lastName")),
                            user.getLastName().toLowerCase()));
                }
                if (user.getEmail() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("email")),
                            user.getEmail().toLowerCase()));
                }
                if (user.getRole() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("role")),
                            user.getRole().name().toLowerCase()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
