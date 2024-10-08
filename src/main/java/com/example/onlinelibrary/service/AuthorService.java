package com.example.onlinelibrary.service;

import java.util.List;

import com.example.onlinelibrary.entity.Author;

public interface AuthorService {
	 
	Author createAuthor(Author author);
    List<Author> getAllAuthors();
    Author getAuthorById(Long id);
    Author updateAuthor(Long id, Author authorDetails);
    void deleteAuthor(Long id);
}
