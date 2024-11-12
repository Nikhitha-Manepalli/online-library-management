package com.example.onlinelibrary.serviceImpl;
 
import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.InvalidDataException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class BookServiceImpl implements BookService {
 
    @Autowired
    private BookRepository bookRepository;
 
    // Transactional management ensures that if something goes wrong, changes are rolled back
    @Transactional
    @Override
    public Book createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidDataException("Book title is required");
        }
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Book with this title already exists");
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
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        return book;
    }
 
    @Transactional
    @Override
    public Book updateBook(Long id, Book bookDetails) {
        if (bookDetails.getTitle() == null || bookDetails.getTitle().isEmpty()) {
            throw new InvalidDataException("Book title is required");
        }
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        try {
            book.setTitle(bookDetails.getTitle());
            book.setPublicationDate(bookDetails.getPublicationDate());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublisher(bookDetails.getPublisher());
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("A book with this title already exists");
        }
    }
 
    @Transactional
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id " + id);
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
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                               book.getAuthor().getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                               book.getPublisher().getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
 
    @Override
    public List<Book> sortBooksByTitle() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
                .collect(Collectors.toList());
    }
 
    @Override
    public List<Book> sortBooksByPublicationDate() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> b1.getPublicationDate().compareTo(b2.getPublicationDate()))
                .collect(Collectors.toList());
    }
 
    @Override
    public List<String> generateAuthorReport() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(book -> book.getAuthor().getName(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue() + " books")
                .collect(Collectors.toList());
    }
}