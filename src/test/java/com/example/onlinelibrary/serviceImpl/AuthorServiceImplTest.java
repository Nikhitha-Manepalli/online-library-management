package com.example.onlinelibrary.serviceImpl;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.service.impl.AuthorServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author();
        author.setId(1L);
        author.setName("Nikhitha");
    }

    @Test
    void createAuthor_ShouldReturnAuthor_WhenAuthorIsValid() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author createdAuthor = authorService.createAuthor(author);

        assertNotNull(createdAuthor);
        assertEquals("Nikhitha", createdAuthor.getName());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void createAuthor_ShouldThrowDataIntegrityException_WhenAuthorAlreadyExists() {
        when(authorRepository.save(any(Author.class))).thenThrow(new DataIntegrityViolationException(""));

        Exception exception = assertThrows(DataIntegrityException.class, () -> {
            authorService.createAuthor(author);
        });

        assertEquals("Author with this name already exists", exception.getMessage());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<Author> authors = authorService.getAllAuthors();

        assertNotNull(authors);
        assertEquals(1, authors.size());
        assertEquals("Nikhitha", authors.get(0).getName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void getAuthorById_ShouldReturnAuthor_WhenExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author foundAuthor = authorService.getAuthorById(1L);

        assertNotNull(foundAuthor);
        assertEquals("Nikhitha", foundAuthor.getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void getAuthorById_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getAuthorById(1L);
        });

        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor_WhenExists() {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Nikhitha");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        Author result = authorService.updateAuthor(1L, updatedAuthor);

        assertNotNull(result);
        assertEquals("Nikhitha", result.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void updateAuthor_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.updateAuthor(1L, author);
        });

        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void deleteAuthor_ShouldDeleteAuthor_WhenExists() {
        when(authorRepository.existsById(1L)).thenReturn(true);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times( 1)).deleteById(1L);
    }

    @Test
    void deleteAuthor_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.deleteAuthor(1L);
        });

        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).existsById(1L);
    }
}