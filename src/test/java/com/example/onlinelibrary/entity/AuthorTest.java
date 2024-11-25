package com.example.onlinelibrary.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorTest {

	@Test
	void testSetBooks() {
		
		Author author = new Author();
		Book book = new Book();
		book.setTitle("Java Fundamentals");
		Set<Book> books = new HashSet<>();
		books.add(book);

		
		author.setBooks(books);

		
		assertEquals(1, author.getBooks().size());
		assertEquals("Java Fundamentals", author.getBooks().iterator().next().getTitle());
	}
}
