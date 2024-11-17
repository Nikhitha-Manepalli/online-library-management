package com.example.onlinelibrary.entity;

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
}