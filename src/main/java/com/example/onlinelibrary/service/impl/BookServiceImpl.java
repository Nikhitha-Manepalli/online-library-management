package com.example.onlinelibrary.service.impl;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.InvalidDataException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.service.BookService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository; 

    private static final String BOOK_NOT_FOUND_MESSAGE = "Book not found with id ";
    private static final String BOOK_TITLE_REQUIRED_MESSAGE = "Book title is required";
    private static final String BOOK_ALREADY_EXISTS_MESSAGE = "Book with this title already exists";

    // Constructor injection
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public Book createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidDataException(BOOK_TITLE_REQUIRED_MESSAGE);
        }
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(BOOK_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE + id);
        }
        return book;
    }

    @Transactional
    @Override
    public Book updateBook(Long id, Book bookDetails) {
        if (bookDetails.getTitle() == null || bookDetails.getTitle().isEmpty()) {
            throw new InvalidDataException(BOOK_TITLE_REQUIRED_MESSAGE);
        }
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE + id);
        }
        try {
            book.setTitle(bookDetails.getTitle());
            book.setPublicationDate(bookDetails.getPublicationDate());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublisher(bookDetails.getPublisher());
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(BOOK_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE + id);
        }
        try {
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Cannot delete book because it is associated with other records");
        }
    }

    @Override
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new InvalidDataException("Search term cannot be empty");
        }
        return bookRepository.findAll().stream()
                .filter(book -> (book.getTitle() != null && book.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) ||
                                (book.getAuthor() != null && book.getAuthor().getName() != null && book.getAuthor().getName().toLowerCase().contains(searchTerm.toLowerCase())) ||
                                (book.getPublisher() != null && book.getPublisher().getName() != null && book.getPublisher().getName().toLowerCase().contains(searchTerm.toLowerCase())))
                .toList();
    }

    @Override
    public List<Book> sortBooksByTitle() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
                .toList();
    }

    @Override
    public List<Book> sortBooksByPublicationDate() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> b1.getPublicationDate().compareTo(b2.getPublicationDate()))
                .toList(); 
    }

    @Override
    public List<String> generateAuthorReport() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(book -> book.getAuthor().getName(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue() + " books")
                .toList(); 
    }
}