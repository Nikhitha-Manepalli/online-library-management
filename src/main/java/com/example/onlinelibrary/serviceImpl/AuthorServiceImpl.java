package com.example.onlinelibrary.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public Author createAuthor(Author author) {
		return authorRepository.save(author);
	}

	@Override
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	@Override
	public Author getAuthorById(Long id) {
		return authorRepository.findById(id).orElse(null);
	}

	@Override
	public Author updateAuthor(Long id, Author authorDetails) {
		Author author = authorRepository.findById(id).orElse(null);
        if (author != null) {
            author.setName(authorDetails.getName());
            return authorRepository.save(author);
        }
        return null;
	}

	@Override
	public void deleteAuthor(Long id) {
		authorRepository.deleteById(id);
		
	}
	
	   
}

