package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        book = new Book(1L, "Effective Java", new Date(), null, null);
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        when(bookService.createBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Effective Java\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));

        verify(bookService, times(1)).createBook(any(Book.class));
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenExists() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        Book updatedBook = new Book(1L, "Effective Java (2nd Edition)", new Date(), null, null);
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Effective Java (2nd Edition)\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java (2nd Edition)"));

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void searchBooks_ShouldReturnListOfBooks() throws Exception {
        when(bookService.searchBooks("Effective")).thenReturn(Collections.singletonList(book));

        mockMvc .perform(get("/api/books/search?searchTerm=Effective"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));

        verify(bookService, times(1)).searchBooks("Effective");
    }

    @Test
    void sortBooksByTitle_ShouldReturnSortedBooks() throws Exception {
        when(bookService.sortBooksByTitle()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books/sort/title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));

        verify(bookService, times(1)).sortBooksByTitle();
    }

    @Test
    void sortBooksByPublicationDate_ShouldReturnSortedBooks() throws Exception {
        when(bookService.sortBooksByPublicationDate()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books/sort/publication-date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));

        verify(bookService, times(1)).sortBooksByPublicationDate();
    }

    @Test
    void generateAuthorReport_ShouldReturnReport() throws Exception {
        when(bookService.generateAuthorReport()).thenReturn(Collections.singletonList("Nikhitha: 1 books"));

        mockMvc.perform(get("/api/books/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Nikhitha: 1 books"));

        verify(bookService, times(1)).generateAuthorReport();
    }
}