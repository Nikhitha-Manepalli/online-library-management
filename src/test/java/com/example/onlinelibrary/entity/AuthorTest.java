package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorTest {

    @Test
    void testAuthorGettersAndSetters() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        assertEquals(1L, author.getId());
        assertEquals("John Doe", author.getName());
    }

    @Test
    void testAuthorBooks() {
        Author author = new Author();
        Book book = new Book();
        book.setTitle("Java Essentials");
        book.setAuthor(author);

        author.getBooks().add(book); // Assuming there is a Set<Book> books in Author

        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(book));
    }

    @Test
    void testRemoveBook() {
        Author author = new Author();
        Book book = new Book();
        book.setTitle("Java Essentials");
        book.setAuthor(author);

        author.getBooks().add(book); // Add book to author's set of books
        author.getBooks().remove(book); // Remove book from author's set of books

        assertEquals(0, author.getBooks().size()); // Check size after removal
        assertFalse(author.getBooks().contains(book)); // Check that the book is no longer in the set
    }
}



/*package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorTest {

    @Test
    void testAuthorGettersAndSetters() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Nikhitha");

        assertEquals(1L, author.getId());
        assertEquals("Nikhitha", author.getName());
    }
}*/