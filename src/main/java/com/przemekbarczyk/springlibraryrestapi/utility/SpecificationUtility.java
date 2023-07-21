package com.przemekbarczyk.springlibraryrestapi.utility;

import com.przemekbarczyk.springlibraryrestapi.model.Book;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationUtility {

    public Specification<Book> getBookSpecification(Book book) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (book != null) {
                if (book.getTitle() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("title")),
                            book.getTitle().toLowerCase()));
                }
                if (book.getAuthor() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("author")),
                            book.getAuthor().toLowerCase()));
                }
                if (book.getPublisher() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("publisher")),
                            book.getPublisher().toLowerCase()));
                }
                if (book.getPublicationDate() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("publicationDate"),book.getPublicationDate()));
                }
                if (book.getStatus() != null) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("status")),
                            book.getStatus().name().toLowerCase()));
                }
                if (book.getReader() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("reader").get("id"), (book.getReader())));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

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
