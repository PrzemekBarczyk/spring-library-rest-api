package com.przemekbarczyk.springlibraryrestapi.service;

import com.przemekbarczyk.springlibraryrestapi.mapper.BookMapper;
import com.przemekbarczyk.springlibraryrestapi.model.Book;
import com.przemekbarczyk.springlibraryrestapi.model.BookStatus;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.repository.BookRepository;
import com.przemekbarczyk.springlibraryrestapi.request.BookRequest;
import com.przemekbarczyk.springlibraryrestapi.utility.PagingUtility;
import com.przemekbarczyk.springlibraryrestapi.utility.SpecificationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    public Page<Book> getSortedPageOfFilteredBooks(
            Book book,
            Integer pageNumber, Integer pageSize,
            String sortBy, String sortDirection) {

        Specification<Book> specification = new SpecificationUtility().getBookSpecification(book);

        Pageable paging = new PagingUtility().getPaging(pageNumber, pageSize, sortBy, sortDirection);

        Page<Book> page = bookRepository.findAll(specification, paging);

        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No books meets criteria");
        }

        return page;
    }

    private List<Book> getBooksByReaderId(Long id) {

        List<Book> books = bookRepository.findByReaderId(id);

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No books are reserved or borrowed");
        }

        return books;
    }

    public Book getBookById(Long id) {

        Optional<Book> book = bookRepository.findById(id);

        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id doesn't exist");
        }

        return book.get();
    }

    public Book addBook(BookRequest bookRequest) {

        BookMapper bookMapper = new BookMapper();
        Book book = bookMapper.mapBookRequestToBook(bookRequest);

        book.setStatus(BookStatus.AVAILABLE);
        book.setReader(null);

        return bookRepository.save(book);
    }

    public Book editBookById(Long originalBookId, Book newData) {

        Book originalBook = getBookById(originalBookId);

        if (newData.getTitle() != null) {
            originalBook.setTitle(newData.getTitle());
        }
        if (newData.getAuthor() != null) {
            originalBook.setAuthor(newData.getAuthor());
        }
        if (newData.getPublisher() != null) {
            originalBook.setPublisher(newData.getPublisher());
        }
        if (newData.getPublicationDate() != null) {
            originalBook.setPublicationDate(newData.getPublicationDate());
        }

        return bookRepository.save(originalBook);
    }

    public Book reserveBookById(Long bookId, Long readerId) {

        Book book = getBookById(bookId);
        User reader = userService.getReaderById(readerId);

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book isn't available");
        }

        int reservedBooks = 0;
        try {
            for (Book b : getBooksByReaderId(readerId)) {
                if (b.getStatus() == BookStatus.RESERVED) {
                    reservedBooks++;
                }
            }
            if (reservedBooks >= 3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit for books reservations has been exceeded");
            }
        }
        catch (ResponseStatusException ex) {
            if (ex.getStatusCode() != HttpStatus.NO_CONTENT) {
                throw ex;
            }
        }

        System.out.println(reservedBooks);

        book.setStatus(BookStatus.RESERVED);
        book.setReader(reader);

        return bookRepository.save(book);
    }

    public Book cancelBookReservationById(Long bookId, Long readerId) {

        Book book = getBookById(bookId);

        if (book.getStatus() != BookStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book isn't reserved");
        }

        if (book.getReader() != null && book.getReader().getId() != readerId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book isn't reserved by reader");
        }

        book.setStatus(BookStatus.AVAILABLE);
        book.setReader(null);

        return bookRepository.save(book);
    }

    public Book borrowBookById(Long bookId, Long readerId) {

        Book book = getBookById(bookId);

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is already on loan");
        }

        if (book.getStatus() == BookStatus.RESERVED && !Objects.equals(book.getReader().getId(), readerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is reserved by different reader");
        }

        int booksOnLoan = 0;
        for (Book b : getBooksByReaderId(readerId)) {
            if (b.getStatus() == BookStatus.BORROWED) {
                booksOnLoan++;
            }
        }
        if (booksOnLoan >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit for books on loan has been exceeded");
        }

        book.setStatus(BookStatus.BORROWED);

        if (book.getReader() == null) { // in case book wasn't reserved
            User reader = userService.getUserById(readerId);
            book.setReader(reader);
        }

        return bookRepository.save(book);
    }

    public Book returnBookById(Long bookId, Long readerId) {

        Book book = getBookById(bookId);

        if (book.getStatus() != BookStatus.BORROWED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book isn't on loan");
        }

        if (book.getReader() != null && book.getReader().getId() != readerId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book isn't loaned by this reader");
        }

        book.setStatus(BookStatus.AVAILABLE);
        book.setReader(null);

        return bookRepository.save(book);
    }

    public Book deleteBookById(Long id) {

        Book book = getBookById(id);

        bookRepository.delete(book);

        return book;
    }
}
