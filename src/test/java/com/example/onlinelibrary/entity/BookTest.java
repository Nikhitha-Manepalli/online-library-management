package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

    @Test
    void testBookGettersAndSetters() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java Essentials");
        book.setPublicationDate(new Date());

        assertEquals(1L, book.getId());
        assertEquals("Java Essentials", book.getTitle());
        assertEquals(book.getPublicationDate(), book.getPublicationDate());
    }

    @Test
    void testBookAuthorSetterAndGetter() {
        Book book = new Book();
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        book.setAuthor(author); // Set the author

        assertEquals(author, book.getAuthor()); // Verify that the author is set correctly
    }

    @Test
    void testBookPublisherSetterAndGetter() {
        Book book = new Book();
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Geetha Publishers");

        book.setPublisher(publisher); // Set the publisher

        assertEquals(publisher, book.getPublisher()); // Verify that the publisher is set correctly
    }
}




/*package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

    @Test
    void testBookGettersAndSetters() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java Essentials");
        book.setPublicationDate(new Date());

        assertEquals(1L, book.getId());
        assertEquals("Java Essentials", book.getTitle());
        assertEquals(book.getPublicationDate(), book.getPublicationDate());
    }
}*/