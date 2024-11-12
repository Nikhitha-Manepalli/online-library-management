package com.example.onlinelibrary.service;
 
import java.util.List;
 
import com.example.onlinelibrary.entity.Book;
 
public interface BookService {
	Book createBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book bookDetails);
    void deleteBook(Long id);
    List<Book> searchBooks(String searchTerm);
    List<Book> sortBooksByTitle();
    List<Book> sortBooksByPublicationDate();
    List<String> generateAuthorReport();
 
}
