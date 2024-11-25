package com.example.onlinelibrary.entity;
 
import org.junit.jupiter.api.Test;
 
import java.util.HashSet;
import java.util.Set;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
 
class PublisherTest {
 
    @Test
    void testSetBooks() {
        
        Publisher publisher = new Publisher();
 
        Book book = new Book();
        book.setTitle("Java Essentials");
 
        
        Set<Book> books = new HashSet<>();
        books.add(book);
 
       
        publisher.setBooks(books);
 
        
        assertEquals(1, publisher.getBooks().size());
        assertEquals("Java Essentials", publisher.getBooks().iterator().next().getTitle());
    }
 
    
 
    
}