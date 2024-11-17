package com.example.onlinelibrary.entity;

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
}