package com.przemekbarczyk.springlibraryrestapi.model;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Title is empty or blank")
    private String title;

    @NotBlank(message = "Author is empty or blank")
    private String author;

    @NotBlank(message = "Publisher is empty or blank")
    private String publisher;

    @NotNull(message = "Publication date is null")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reader;

    @PrePersist
    @PreUpdate
    private void validate() {

        boolean bookIsAvailable = status == BookStatus.AVAILABLE;
        boolean bookHasAssignUser = reader != null;

        if (bookIsAvailable && bookHasAssignUser) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Available book can't have associated reader");
        }
        else if (!bookIsAvailable) {
            if (bookHasAssignUser && reader.getRole() != UserRole.READER) { // book has associated user with wrong role
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Reserved or borrowed book must have associated user with reader role");
            }
            else if (!bookHasAssignUser) { // book is reserved or borrowed and doesn't have associated user
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Reserved or borrowed book must have associated user with reader role");
            }
        }
    }
}
