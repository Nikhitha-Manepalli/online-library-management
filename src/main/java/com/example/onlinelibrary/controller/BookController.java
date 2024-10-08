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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	    @Autowired
	    private BookService bookService;
	 
	    @PostMapping
	    public Book createBook(@RequestBody Book book) {
	        return bookService.createBook(book);
	    }
	 
	    @GetMapping
	    public List<Book> getAllBooks() {
	        return bookService.getAllBooks();
	    }
	 
	    @GetMapping("/{id}")
	    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
	        Book book = bookService.getBookById(id);
	        if (book != null) {
	            return ResponseEntity.ok(book);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @PutMapping("/{id}")
	    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
	        Book updatedBook = bookService.updateBook(id, bookDetails);
	        if (updatedBook != null) {
	            return ResponseEntity.ok(updatedBook);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
	        bookService.deleteBook(id);
	        return ResponseEntity.noContent().build();
	    }
	 
	    @GetMapping("/search")
	    public List<Book> searchBooks(@RequestParam String searchTerm) {
	        return bookService.searchBooks(searchTerm);
	    }
	 
	    @GetMapping("/sort/title")
	    public List<Book> sortBooksByTitle() {
	        return bookService.sortBooksByTitle();
	    }
	 
	    @GetMapping("/sort/publication-date")
	    public List<Book> sortBooksByPublicationDate() {
	        return bookService.sortBooksByPublicationDate();
	    }
	 
	    @GetMapping("/report")
	    public List<String> generateAuthorReport() {
	        return bookService.generateAuthorReport();
	    }
}
