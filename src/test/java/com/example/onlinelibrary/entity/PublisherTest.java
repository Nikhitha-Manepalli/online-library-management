package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublisherTest {

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
    }

    @Test
    void testPublisherGettersAndSetters() {
        publisher.setId(1L);
        publisher.setName("Geetha Publishers");

        assertEquals(1L, publisher.getId());
        assertEquals("Geetha Publishers", publisher.getName());
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setPublisher(publisher);

        publisher.getBooks().add(book); // Add book to publisher's set of books

        assertEquals(1, publisher.getBooks().size());
        assertTrue(publisher.getBooks().contains(book));
    }

    @Test
    void testRemoveBook() {
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setPublisher(publisher);

        publisher.getBooks().add(book); // Add book to publisher's set of books
        publisher.getBooks().remove(book); // Remove book from publisher's set of books

        assertEquals(0, publisher.getBooks().size()); // Check size after removal
        assertFalse(publisher.getBooks().contains(book)); // Check that the book is no longer in the set
    }

    @Test
    void testBooksInitialization() {
        assertEquals(0, publisher.getBooks().size()); // Check that books set is initialized
    }
}


/*package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublisherTest {

    @Test
    void testPublisherGettersAndSetters() {
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Geetha Publishers");

        assertEquals(1L, publisher.getId());
        assertEquals("Geetha Publishers", publisher.getName());
    }
}*/