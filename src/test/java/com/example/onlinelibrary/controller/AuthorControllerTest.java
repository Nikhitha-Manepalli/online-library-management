package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.service.AuthorService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        author = new Author(1L, "Nikhitha", Collections.emptySet());
    }

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() throws Exception {
        when(authorService.createAuthor(any(Author.class))).thenReturn(author);

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Nikhitha\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nikhitha"));

        verify(authorService, times(1)).createAuthor(any(Author.class));
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nikhitha"));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void getAuthorById_ShouldReturnAuthor_WhenExists() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(author);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nikhitha"));

        verify(authorService, times(1)).getAuthorById(1L);
    }

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor() throws Exception {
        Author updatedAuthor = new Author(1L, "Nikhitha", Collections.emptySet());
        when(authorService.updateAuthor(eq(1L), any(Author.class))).thenReturn(updatedAuthor);

        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Nikhitha\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nikhitha"));

        verify(authorService, times(1)).updateAuthor(eq(1L), any(Author.class));
    }

    @Test
    void deleteAuthor_ShouldReturnNoContent() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());

        verify(authorService, times(1)).deleteAuthor(1L);
    }
}