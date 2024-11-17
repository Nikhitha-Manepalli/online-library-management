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
}