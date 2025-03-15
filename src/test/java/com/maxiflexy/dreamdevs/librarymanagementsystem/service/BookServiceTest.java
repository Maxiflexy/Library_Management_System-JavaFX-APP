package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BookDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookDAO bookDAO;

    private BookService bookService;

    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        when(bookDAO.getAllBooks()).thenReturn(new ArrayList<>());

        bookService = new BookService(bookDAO);

        // Create test books
        book1 = new Book(1, "Test Book 1", "Author 1", "Fiction", 5);
        book2 = new Book(2, "Test Book 2", "Author 2", "Non-fiction", 3);
        book3 = new Book(3, "Java Programming", "Author 3", "Technical", 7);

        // Clear initial interaction from constructor
        clearInvocations(bookDAO);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(book1, book2, book3);
        when(bookDAO.getAllBooks()).thenReturn(mockBooks);

        // Need to trigger a refresh to use our new mock data
        bookService.refreshCache();

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertEquals(3, result.size());
        assertEquals(mockBooks, result);
        verify(bookDAO).getAllBooks();
    }

    @Test
    void testGetBooksByPage() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(book1, book2, book3);
        when(bookDAO.getAllBooks()).thenReturn(mockBooks);

        // Need to trigger a refresh to use our new mock data
        bookService.refreshCache();

        // Act - Get first page with 2 items per page
        List<Book> page1 = bookService.getBooksByPage(0, 2);

        // Assert
        assertEquals(2, page1.size());
        assertEquals(book1, page1.get(0));
        assertEquals(book2, page1.get(1));

        // Act - Get second page with 2 items per page
        List<Book> page2 = bookService.getBooksByPage(1, 2);

        // Assert
        assertEquals(1, page2.size());
        assertEquals(book3, page2.get(0));
    }

    @Test
    void testAddBook() {
        // Arrange
        Book newBook = new Book("New Book", "New Author", "Mystery", 10);

        // Act
        bookService.addBook(newBook);

        // Assert
        verify(bookDAO).addBook(newBook);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book bookToUpdate = new Book(1, "Updated Title", "Updated Author", "Updated Genre", 8);

        // Act
        bookService.updateBook(bookToUpdate);

        // Assert
        verify(bookDAO).updateBook(bookToUpdate);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        int bookId = 1;

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookDAO).deleteBook(bookId);
    }

    @Test
    void testSearchBooksByTitle() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(book1, book2, book3);
        when(bookDAO.getAllBooks()).thenReturn(mockBooks);

        // Need to trigger a refresh
        bookService.refreshCache();

        // Act
        List<Book> result = bookService.searchBooksByTitle("Java");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
    }

    @Test
    void testSearchBooksByAuthor() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(book1, book2, book3);
        when(bookDAO.getAllBooks()).thenReturn(mockBooks);

        // Need to trigger a refresh
        bookService.refreshCache();

        // Act
        List<Book> result = bookService.searchBooksByAuthor("Author 2");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Author 2", result.get(0).getAuthor());
    }

    @Test
    void testSortBooksByTitle() {
        // Arrange
        List<Book> unsortedBooks = Arrays.asList(book3, book1, book2);
        when(bookDAO.getAllBooks()).thenReturn(unsortedBooks);

        // Need to trigger a refresh
        bookService.refreshCache();

        // Act
        List<Book> sortedBooks = bookService.sortBooksByTitle();

        // Assert
        assertEquals(3, sortedBooks.size());
        assertEquals("Java Programming", sortedBooks.get(0).getTitle());
        assertEquals("Test Book 1", sortedBooks.get(1).getTitle());
        assertEquals("Test Book 2", sortedBooks.get(2).getTitle());
    }

    @Test
    void testUpdateBookAvailability() {
        // Arrange
        int bookId = 1;
        when(bookDAO.updateBookAvailability(bookId, true)).thenReturn(true);

        // Mock the getAllBooks() that will be called by refreshCache() inside updateBookAvailability()
        when(bookDAO.getAllBooks()).thenReturn(Arrays.asList(book1, book2, book3));

        // Act
        boolean result = bookService.updateBookAvailability(bookId, true);

        // Assert
        assertTrue(result);
        verify(bookDAO).updateBookAvailability(bookId, true);

        // Verify getAllBooks was called during the refresh
        verify(bookDAO).getAllBooks();
    }
}
