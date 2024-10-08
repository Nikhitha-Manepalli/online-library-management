package com.example.onlinelibrary.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.service.BookService;

@Service
public class BookServiceImpl  implements BookService {
	
	@Autowired
	private BookRepository bookRepository;

	@Override
	public Book createBook(Book book) {
		   return bookRepository.save(book);
	}

	@Override
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public Book updateBook(Long id, Book bookDetails) {
		Book book = bookRepository.findById(id).orElse(null);
	    if (book != null) {
	        book.setTitle(bookDetails.getTitle());
	        book.setPublicationDate(bookDetails.getPublicationDate());
	        book.setAuthor(bookDetails.getAuthor());
	        book.setPublisher(bookDetails.getPublisher());
	        return bookRepository.save(book);
	    }
	    return null;
	}

	@Override
	public void deleteBook(Long id) {
		 bookRepository.deleteById(id);
		
	}

	@Override
	public List<Book> searchBooks(String searchTerm) {
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


