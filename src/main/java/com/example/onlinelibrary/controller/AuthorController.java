package com.example.onlinelibrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.service.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
	 
	    @Autowired
	    private AuthorService authorService;
	 
	    @PostMapping
	    public Author createAuthor(@RequestBody Author author) {
	        return authorService.createAuthor(author);
	    }
	 
	    @GetMapping
	    public List<Author> getAllAuthors() {
	        return authorService.getAllAuthors();
	    }
	 
	    @GetMapping("/{id}")
	    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
	        Author author = authorService.getAuthorById(id);
	        if (author != null) {
	            return ResponseEntity.ok(author);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @PutMapping("/{id}")
	    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
	        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
	        if (updatedAuthor != null) {
	            return ResponseEntity.ok(updatedAuthor);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
	        authorService.deleteAuthor(id);
	        return ResponseEntity.noContent().build();
	    }
	
}
