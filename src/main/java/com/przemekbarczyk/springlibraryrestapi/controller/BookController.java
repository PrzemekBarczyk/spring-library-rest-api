package com.przemekbarczyk.springlibraryrestapi.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.przemekbarczyk.springlibraryrestapi.model.Book;
import com.przemekbarczyk.springlibraryrestapi.request.BookRequest;
import com.przemekbarczyk.springlibraryrestapi.security.UserPrincipal;
import com.przemekbarczyk.springlibraryrestapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/librarian/books")
    public ResponseEntity<Page<Book>> getSortedPageOfFilteredBooks(
            @RequestBody(required = false) Book filters,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue =  "100") int pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ascending") String sortDirection) {

        Page<Book> books = bookService.getSortedPageOfFilteredBooks(
                filters,
                pageNumber, pageSize,
                sortBy, sortDirection);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/reader/books")
    @JsonIgnoreProperties(value = "reader")
    public ResponseEntity<Page<Book>> getSortedPageOfBooksWithoutReader(
            @RequestBody(required = false) Book book,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue =  "100") int pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ascending") String sortDirection) {

        Page<Book> books = bookService.getSortedPageOfFilteredBooks(
                book,
                pageNumber, pageSize,
                sortBy, sortDirection);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/reader/books/user/logged")
    public ResponseEntity<List<Book>> getLoggedReaderBooks(@AuthenticationPrincipal UserPrincipal principal) {

        List<Book> books = bookService.getBooksByReaderPrincipal(principal);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }



    @GetMapping("/librarian/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {

        Book book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/reader/book/{id}")
    @JsonIgnoreProperties(value = "reader")
    public ResponseEntity<Book> getBookByIdWithoutReader(@PathVariable Long id) {

        Book book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }



    @PostMapping("/librarian/book")
    public ResponseEntity<Book> addBook(@RequestBody @Validated BookRequest bookRequest) {

        Book addedBook = bookService.addBook(bookRequest);
        return new ResponseEntity<>(addedBook, HttpStatus.OK);
    }



    @PostMapping("/librarian/book/{id}")
    public ResponseEntity<Book> editBookById(@PathVariable Long originalBookId, @RequestBody Book newData) {

        Book updatedBook = bookService.editBookById(originalBookId, newData);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }



    @GetMapping("/librarian/book/{bookId}/reader/{readerId}/reserve")
    public ResponseEntity<Book> reserveBookById(@PathVariable Long bookId, @PathVariable Long readerId) {

        Book reservedBook = bookService.reserveBookById(bookId, readerId);
        return new ResponseEntity<>(reservedBook, HttpStatus.OK);
    }

    @GetMapping("/reader/book/{bookId}/reader/logged/reserve")
    public ResponseEntity<Book> reserveBookByIdByLoggedReader(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserPrincipal principal) {

        Book reservedBook = bookService.reserveBookById(bookId, principal.getId());
        return new ResponseEntity<>(reservedBook, HttpStatus.OK);
    }

    @GetMapping("/librarian/book/{bookId}/reader/{readerId}/cancel-reservation")
    public ResponseEntity<Book> cancelBookReservationById(@PathVariable Long bookId, @PathVariable Long readerId) {

        Book canceledBook = bookService.cancelBookReservationById(bookId, readerId);
        return new ResponseEntity<>(canceledBook, HttpStatus.OK);
    }

    @GetMapping("/reader/book/{bookId}/reader/logged/cancel-reservation")
    public ResponseEntity<Book> cancelBookReservationByIdByLoggedReader(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserPrincipal principal) {

        Book canceledBook = bookService.cancelBookReservationById(bookId, principal.getId());
        return new ResponseEntity<>(canceledBook, HttpStatus.OK);
    }



    @GetMapping("/librarian/book/{bookId}/reader/{readerId}/borrow")
    public ResponseEntity<Book> borrowBookById(@PathVariable Long bookId, @PathVariable Long readerId) {

        Book loanedBook = bookService.borrowBookById(bookId, readerId);
        return new ResponseEntity<>(loanedBook, HttpStatus.OK);
    }

    @GetMapping("/librarian/book/{bookId}/reader/{readerId}/return")
    public ResponseEntity<Book> returnBookById(@PathVariable Long bookId, @PathVariable Long readerId) {

        Book returnedBook = bookService.returnBookById(bookId, readerId);
        return new ResponseEntity<>(returnedBook, HttpStatus.OK);
    }



    @DeleteMapping("/librarian/book/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable Long id) {

        Book deletedBook = bookService.deleteBookById(id);
        return new ResponseEntity<>(deletedBook, HttpStatus.OK);
    }
}
