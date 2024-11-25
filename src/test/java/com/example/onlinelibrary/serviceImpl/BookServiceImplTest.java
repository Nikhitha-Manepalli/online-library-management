package com.example.onlinelibrary.serviceImpl;

import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.entity.Book;
import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.InvalidDataException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.BookRepository;
import com.example.onlinelibrary.service.impl.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	private Book book;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		book = new Book();
		book.setId(1L);
		book.setTitle("Effective Java");
		book.setPublicationDate(new Date());

	}

	@Test
	void createBook_ShouldReturnBook_WhenBookIsValid() {
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book createdBook = bookService.createBook(book);

		assertNotNull(createdBook);
		assertEquals("Effective Java", createdBook.getTitle());
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	void createBook_ShouldThrowInvalidDataException_WhenTitleIsEmpty() {
		book.setTitle(null);

		Exception exception = assertThrows(InvalidDataException.class, () -> {
			bookService.createBook(book);
		});

		assertEquals("Book title is required", exception.getMessage());
		verify(bookRepository, never()).save(any(Book.class));
	}

	@Test
	void createBook_ShouldThrowDataIntegrityException_WhenBookAlreadyExists() {
		when(bookRepository.save(any(Book.class))).thenThrow(new DataIntegrityViolationException(""));

		Exception exception = assertThrows(DataIntegrityException.class, () -> {
			bookService.createBook(book);
		});

		assertEquals("Book with this title already exists", exception.getMessage());
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	void getAllBooks_ShouldReturnListOfBooks() {
		when(bookRepository.findAll()).thenReturn(List.of(book));

		List<Book> books = bookService.getAllBooks();

		assertNotNull(books);
		assertEquals(1, books.size());
		assertEquals("Effective Java", books.get(0).getTitle());
		verify(bookRepository, times(1)).findAll();
	}

	@Test
	void getBookById_ShouldReturnBook_WhenExists() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		Book foundBook = bookService.getBookById(1L);

		assertNotNull(foundBook);
		assertEquals("Effective Java", foundBook.getTitle());
		verify(bookRepository, times(1)).findById(1L);
	}

	@Test
	void getBookById_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
		when(bookRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.getBookById(1L);
		});

		assertEquals("Book not found with id 1", exception.getMessage());
		verify(bookRepository, times(1)).findById(1L);
	}

	@Test
	void updateBook_ShouldReturnUpdatedBook_WhenExists() {
		Book updatedBook = new Book();
		updatedBook.setTitle("Effective Java (2nd Edition)");
		updatedBook.setPublicationDate(new Date());

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

		Book result = bookService.updateBook(1L, updatedBook);

		assertNotNull(result);
		assertEquals("Effective Java (2nd Edition)", result.getTitle());
		verify(bookRepository, times(1)).findById(1L);
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	void updateBook_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
		when(bookRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.updateBook(1L, book);
		});

		assertEquals("Book not found with id 1", exception.getMessage());
		verify(bookRepository, times(1)).findById(1L);
	}

	@Test
	void deleteBook_ShouldDeleteBook_WhenExists() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		bookService.deleteBook(1L);

		verify(bookRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteBook_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
		when(bookRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			bookService.deleteBook(1L);
		});

		assertEquals("Book not found with id 1", exception.getMessage());
		verify(bookRepository, times(1)).findById(1L);
	}

	@Test
	void searchBooks_ShouldReturnListOfBooks_WhenSearchTermMatches() {
		when(bookRepository.findAll()).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks("Effective");

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Effective Java", result.get(0).getTitle());
	}

	@Test
	void searchBooks_ShouldThrowInvalidDataException_WhenSearchTermIsEmpty() {
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			bookService.searchBooks("");
		});

		assertEquals("Search term cannot be empty", exception.getMessage());
	}

	@Test
	void sortBooksByTitle_ShouldReturnSortedBooks() {
		Book book2 = new Book();
		book2.setTitle("Java Concurrency in Practice");
		List<Book> books = new ArrayList<>();
		books.add(book);
		books.add(book2);

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> sortedBooks = bookService.sortBooksByTitle();

		assertEquals("Effective Java", sortedBooks.get(0).getTitle());
		assertEquals("Java Concurrency in Practice", sortedBooks.get(1).getTitle());
	}

	@Test
	void sortBooksByPublicationDate_ShouldReturnSortedBooks() {

		Book book2 = new Book();
		book2.setTitle("Java Concurrency in Practice");
		book2.setPublicationDate(new Date(System.currentTimeMillis() - 1000000));
		List<Book> books = new ArrayList<>();
		books.add(book);
		books.add(book2);

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> sortedBooks = bookService.sortBooksByPublicationDate();

		assertEquals(book2.getTitle(), sortedBooks.get(0).getTitle());
		assertEquals(book.getTitle(), sortedBooks.get(1).getTitle());
	}

	@Test
	void generateAuthorReport_ShouldReturnReport() {

		Author author = new Author();
		author.setName("Nikhitha");

		book.setAuthor(author);

		when(bookRepository.findAll()).thenReturn(List.of(book));

		List<String> report = bookService.generateAuthorReport();

		assertNotNull(report);
		assertEquals(1, report.size());
		assertEquals("Nikhitha: 1 books", report.get(0));
	}

	@Test

	void createBook_ShouldThrowInvalidDataException_WhenTitleIsNull() {

		book.setTitle(null);

		InvalidDataException exception = assertThrows(InvalidDataException.class, () -> bookService.createBook(book));

		assertEquals("Book title is required", exception.getMessage());

	}

	@Test

	void updateBook_ShouldThrowInvalidDataException_WhenTitleIsEmpty() {

		book.setTitle("");

		InvalidDataException exception = assertThrows(InvalidDataException.class,
				() -> bookService.updateBook(1L, book));

		assertEquals("Book title is required", exception.getMessage());

	}

	@Test
	void searchBooks_ShouldReturnEmptyList_WhenNoBooksMatch() {
		when(bookRepository.findAll()).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks("Nonexistent Book");

		assertNotNull(result);
		assertTrue(result.isEmpty(), "Expected no books to match the search term");
	}

	@Test

	public void testCreateBook_Success() {

		Book book = new Book();

		book.setTitle("New Book");

		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

		Book createdBook = bookService.createBook(book);

		assertNotNull(createdBook);

		assertEquals("New Book", createdBook.getTitle());

	}

	@Test

	public void testCreateBook_EmptyTitle_ThrowsInvalidDataException() {

		Book book = new Book();

		book.setTitle("");

		InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {

			bookService.createBook(book);

		});

		assertEquals("Book title is required", exception.getMessage());

	}

	@Test

	public void testCreateBook_DuplicateTitle_ThrowsDataIntegrityException() {

		Book book = new Book();

		book.setTitle("Duplicate Title");

		Mockito.when(bookRepository.save(Mockito.any(Book.class)))

				.thenThrow(new DataIntegrityViolationException("Duplicate"));

		DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> {

			bookService.createBook(book);

		});

		assertEquals("Book with this title already exists", exception.getMessage());

	}

	@Test

	void whenTitleIsNull_thenThrowsInvalidDataException() {

		Book book = new Book();

		book.setTitle(null);

		assertThrows(InvalidDataException.class, () -> {

			bookService.updateBook(1L, book);

		});

	}

	@Test

	void whenDuplicateBook_thenThrowsDataIntegrityException() {

		Book book = new Book();

		book.setTitle("Existing Book");

		when(bookRepository.save(any(Book.class))).thenThrow(new DataIntegrityViolationException(""));

		assertThrows(DataIntegrityException.class, () -> {

			bookService.createBook(book);

		});

	}

	@Test

	void whenBookIsAssociatedWithOtherRecords_thenThrowsDataIntegrityException() {

		Book book = new Book();

		book.setTitle("Book to Delete");

		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

		doThrow(new DataIntegrityViolationException("")).when(bookRepository).deleteById(anyLong());

		assertThrows(DataIntegrityException.class, () -> {

			bookService.deleteBook(1L);

		});

	}

	@Test

	void whenSearchTermIsEmpty_thenThrowsInvalidDataException() {

		assertThrows(InvalidDataException.class, () -> {

			bookService.searchBooks("");

		});

		assertThrows(InvalidDataException.class, () -> {

			bookService.searchBooks(null);

		});

	}

	@Test

	void whenSearchTermMatchesTitle_thenReturnsFilteredBooks() {

		Book book1 = new Book();

		book1.setTitle("Java Programming");

		Book book2 = new Book();

		book2.setTitle("Spring Boot Guide");

		List<Book> books = Arrays.asList(book1, book2);

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> result = bookService.searchBooks("java");

		assertEquals(1, result.size());

		assertEquals("Java Programming", result.get(0).getTitle());

	}
	
	@Test
	void searchBooks_ShouldReturnBooks_WhenTitleMatches() {
	    Book book1 = new Book();
	    book1.setTitle("Clean Code");
	    Book book2 = new Book();
	    book2.setTitle("Effective Java");
	    when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
	    List<Book> result = bookService.searchBooks("Clean");
	    assertEquals(1, result.size());
	    assertEquals("Clean Code", result.get(0).getTitle());
	}
	@Test
	void searchBooks_ShouldReturnBooks_WhenAuthorMatches() {
	    Author author = new Author();
	    author.setName("Robert C. Martin");
	    Book book = new Book();
	    book.setTitle("Clean Code");
	    book.setAuthor(author);
	    when(bookRepository.findAll()).thenReturn(List.of(book));
	    List<Book> result = bookService.searchBooks("Robert");
	    assertEquals(1, result.size());
	    assertEquals("Clean Code", result.get(0).getTitle());
	}
	@Test
	void searchBooks_ShouldReturnBooks_WhenPublisherMatches() {
	    Publisher publisher = new Publisher();
	    publisher.setName("Prentice Hall");
	    Book book = new Book();
	    book.setTitle("Clean Code");
	    book.setPublisher(publisher);
	    when(bookRepository.findAll()).thenReturn(List.of(book));
	    List<Book> result = bookService.searchBooks("Prentice");
	    assertEquals(1, result.size());
	    assertEquals("Clean Code", result.get(0).getTitle());
	}
	@Test
	void searchBooks_ShouldReturnEmptyList_WhenNoMatchFound() {
	    Book book1 = new Book();
	    book1.setTitle("Clean Code");
	    Book book2 = new Book();
	    book2.setTitle("Effective Java");
	    when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
	    List<Book> result = bookService.searchBooks("Nonexistent");
	    assertTrue(result.isEmpty());
	}
	
	
	@Test
	void searchBooks_ShouldReturnBooks_WhenSearchTermMatchesMultipleFields() {
	    Author author = new Author();
	    author.setName("John Doe");
	    Publisher publisher = new Publisher();
	    publisher.setName("Pearson");
	    Book book = new Book();
	    book.setTitle("Java Programming");
	    book.setAuthor(author);
	    book.setPublisher(publisher);
	 
	    when(bookRepository.findAll()).thenReturn(List.of(book));
	 
	    List<Book> result = bookService.searchBooks("John");
	    assertNotNull(result);
	    assertEquals(1, result.size());
	    assertEquals("Java Programming", result.get(0).getTitle());
	}
	@Test
	void updateBook_ShouldThrowDataIntegrityException_WhenDuplicateBookExists() {
	   
	    Book existingBook = new Book();
	    existingBook.setId(1L);
	    existingBook.setTitle("Duplicate Title");
	    existingBook.setPublicationDate(new Date());
	 
	    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
	    when(bookRepository.save(any(Book.class))).thenThrow(new DataIntegrityViolationException("Duplicate title"));
	 
	    DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> {
	        bookService.updateBook(1L, existingBook);
	    });
	 
	    assertEquals("Book with this title already exists", exception.getMessage());
	    verify(bookRepository, times(1)).findById(1L);
	    verify(bookRepository, times(1)).save(any(Book.class));
	}

	
	
}