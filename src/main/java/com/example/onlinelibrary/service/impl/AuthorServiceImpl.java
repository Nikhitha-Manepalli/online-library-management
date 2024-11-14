package com.example.onlinelibrary.service.impl;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.service.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private static final String AUTHOR_NOT_FOUND_MESSAGE = "Author not found with id ";
    private static final String AUTHOR_ALREADY_EXISTS_MESSAGE = "Author with this name already exists";

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author createAuthor(Author author) {
        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(AUTHOR_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR_NOT_FOUND_MESSAGE + id));
        author.setName(authorDetails.getName());

        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(AUTHOR_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException(AUTHOR_NOT_FOUND_MESSAGE + id);
        }
        authorRepository.deleteById(id);
    }
}