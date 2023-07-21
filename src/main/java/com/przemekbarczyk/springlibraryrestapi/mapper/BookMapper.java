package com.przemekbarczyk.springlibraryrestapi.mapper;

import com.przemekbarczyk.springlibraryrestapi.model.Book;
import com.przemekbarczyk.springlibraryrestapi.request.BookRequest;

public class BookMapper {

    public Book mapBookRequestToBook(BookRequest bookRequest) {

        Book book = new Book();

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublisher(bookRequest.getPublisher());
        book.setPublicationDate(bookRequest.getPublicationDate());

        return book;
    }
}
