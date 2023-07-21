package com.przemekbarczyk.springlibraryrestapi.request;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class BookRequest {

    @NotBlank(message = "Title is empty or blank")
    private String title;

    @NotBlank(message = "Author is empty or blank")
    private String author;

    @NotBlank(message = "Publisher is empty or blank")
    private String publisher;

    @NotNull(message = "Publication date is null")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;
}
