package com.example.onlinelibrary.serviceImpl;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.InvalidDataException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.service.impl.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setPublicationDate(new Date());
        // Assuming Author and Publisher are set properly as well
    }

    @Test
    void createBook_ShouldReturnBook_WhenBookIsValid() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals("Effective Java", createdBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_ShouldThrowInvalidDataException_WhenTitleIsEmpty() {
        book.setTitle(null);

        Exception exception = assertThrows(InvalidDataException.class, () -> {
            bookService.createBook(book);
        });

        assertEquals("Book title is required", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_ShouldThrowDataIntegrityException_WhenBookAlreadyExists() {
        when(bookRepository.save(any(Book.class))).thenThrow(new DataIntegrityViolationException(""));

        Exception exception = assertThrows(DataIntegrityException.class, () -> {
            bookService.createBook(book);
        });

        assertEquals("Book with this title already exists", exception.getMessage());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("Effective Java", foundBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook_WhenExists() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Effective Java (2nd Edition)");
        updatedBook.setPublicationDate(new Date());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("Effective Java (2nd Edition)", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, book);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void searchBooks_ShouldReturnListOfBooks_WhenSearchTermMatches() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks("Effective");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    void searchBooks_ShouldThrowInvalidDataException_WhenSearchTermIsEmpty() {
        Exception exception = assertThrows(InvalidDataException.class, () -> {
            bookService.searchBooks("");
        });

        assertEquals("Search term cannot be empty", exception.getMessage());
    }

    @Test
    void sortBooksByTitle_ShouldReturnSortedBooks() {
        Book book2 = new Book();
        book2.setTitle("Java Concurrency in Practice");
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> sortedBooks = bookService.sortBooksByTitle();

        assertEquals("Effective Java", sortedBooks.get(0).getTitle());
        assertEquals("Java Concurrency in Practice", sortedBooks.get(1).getTitle());
    }

    @Test
    void sortBooksByPublicationDate_ShouldReturnSortedBooks() {
        // Assuming books have different publication dates
        Book book2 = new Book();
        book2.setTitle("Java Concurrency in Practice");
        book2.setPublicationDate(new Date(System.currentTimeMillis() - 1000000)); // earlier date
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> sortedBooks = bookService.sortBooksByPublicationDate();

        assertEquals(book2.getTitle(), sortedBooks.get(0).getTitle());
        assertEquals(book.getTitle(), sortedBooks.get(1).getTitle());
    }

    @Test
    void generateAuthorReport_ShouldReturnReport() {
        // Create an Author object and set it to the book
        Author author = new Author();
        author.setName("John Doe");
        
        book.setAuthor(author); // Set the author to the book

        // Mock the repository to return the book
        when(bookRepository.findAll()).thenReturn(List.of(book));

        // Call the method to generate the report
        List<String> report = bookService.generateAuthorReport();

        // Assertions to validate the report
        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals("John Doe: 1 books", report.get(0)); 
    }
}