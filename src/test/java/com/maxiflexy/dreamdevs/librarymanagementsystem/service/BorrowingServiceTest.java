package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BorrowingDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.BorrowingRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {

    @Mock
    private BorrowingDAO borrowingDAO;

    @Mock
    private BookService bookService;

    private BorrowingService borrowingService;

    private BorrowingRecord record1;
    private BorrowingRecord record2;
    private BorrowingRecord record3;

    @BeforeEach
    void setUp() {
        // Create test records
        record1 = new BorrowingRecord(1, 1, 1, Date.valueOf(LocalDate.now().minusDays(10)), null);
        record1.setBookTitle("Book 1");
        record1.setMemberName("Member 1");

        record2 = new BorrowingRecord(2, 2, 2, Date.valueOf(LocalDate.now().minusDays(5)), Date.valueOf(LocalDate.now()));
        record2.setBookTitle("Book 2");
        record2.setMemberName("Member 2");

        record3 = new BorrowingRecord(3, 3, 1, Date.valueOf(LocalDate.now().minusDays(2)), null);
        record3.setBookTitle("Book 3");
        record3.setMemberName("Member 1");

        // Setup active records BEFORE creating the service
        when(borrowingDAO.getActiveBorrowingRecords()).thenReturn(Arrays.asList(record1, record3));

        // Now create the service with properly setup mocks
        borrowingService = new BorrowingService(borrowingDAO, bookService);

        // Clear initial interactions to start fresh for each test
        clearInvocations(borrowingDAO, bookService);
    }

    @Test
    void testBorrowBook() {
        // Arrange
        int bookId = 4;
        int memberId = 1;
        when(bookService.updateBookAvailability(bookId, false)).thenReturn(true);

        // Act
        boolean result = borrowingService.borrowBook(bookId, memberId);

        // Assert
        assertTrue(result);
        verify(bookService).updateBookAvailability(bookId, false);
        verify(borrowingDAO).addBorrowingRecord(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBookWhenNotAvailable() {
        // Arrange
        int bookId = 5;
        int memberId = 2;
        when(bookService.updateBookAvailability(bookId, false)).thenReturn(false);

        // Act
        boolean result = borrowingService.borrowBook(bookId, memberId);

        // Assert
        assertFalse(result);
        verify(bookService).updateBookAvailability(bookId, false);
        verify(borrowingDAO, never()).addBorrowingRecord(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook() {
        // Arrange
        int recordId = 1;
        when(borrowingDAO.getBorrowingRecordById(recordId)).thenReturn(record1);

        // Act
        boolean result = borrowingService.returnBook(recordId);

        // Assert
        assertTrue(result);
        verify(borrowingDAO).updateReturnDate(eq(recordId), any(Date.class));
        verify(bookService).updateBookAvailability(record1.getBookId(), true);
    }

    @Test
    void testReturnBookAlreadyReturned() {
        // Arrange
        int recordId = 2;
        when(borrowingDAO.getBorrowingRecordById(recordId)).thenReturn(record2);

        // Act
        boolean result = borrowingService.returnBook(recordId);

        // Assert
        assertFalse(result);
        verify(borrowingDAO, never()).updateReturnDate(eq(recordId), any(Date.class));
        verify(bookService, never()).updateBookAvailability(anyInt(), anyBoolean());
    }

    @Test
    void testGetAllBorrowingRecords() {
        // Arrange
        List<BorrowingRecord> mockRecords = Arrays.asList(record1, record2, record3);
        when(borrowingDAO.getAllBorrowingRecords()).thenReturn(mockRecords);

        // Act
        List<BorrowingRecord> result = borrowingService.getAllBorrowingRecords();

        // Assert
        assertEquals(3, result.size());
        assertEquals(mockRecords, result);
    }

    @Test
    void testGetBorrowingRecordsByPage() {
        // Arrange
        List<BorrowingRecord> mockRecords = Arrays.asList(record1, record2, record3);
        when(borrowingDAO.getAllBorrowingRecords()).thenReturn(mockRecords);

        // Act - Get first page with 2 items per page
        List<BorrowingRecord> page1 = borrowingService.getBorrowingRecordsByPage(0, 2);

        // Assert
        assertEquals(2, page1.size());
        assertEquals(record1, page1.get(0));
        assertEquals(record2, page1.get(1));

        // Act - Get second page with 2 items per page
        List<BorrowingRecord> page2 = borrowingService.getBorrowingRecordsByPage(1, 2);

        // Assert
        assertEquals(1, page2.size());
        assertEquals(record3, page2.get(0));
    }

    @Test
    void testGetActiveBorrowingRecords() {
        // Act
        List<BorrowingRecord> result = borrowingService.getActiveBorrowingRecords();

        // Assert
        assertEquals(2, result.size());
        assertSame(record1, result.get(0));
        assertSame(record3, result.get(1));
    }

    @Test
    void testGetBorrowedBooksMap() {
        // Act
        Map<Integer, Integer> borrowedBooks = borrowingService.getBorrowedBooksMap();

        // Assert
        assertEquals(2, borrowedBooks.size());
        assertTrue(borrowedBooks.containsKey(record1.getBookId()));
        assertTrue(borrowedBooks.containsKey(record3.getBookId()));
        assertEquals(record1.getMemberId(), borrowedBooks.get(record1.getBookId()));
        assertEquals(record3.getMemberId(), borrowedBooks.get(record3.getBookId()));
    }

    @Test
    void testIsBookBorrowed() {
        // Act & Assert
        assertTrue(borrowingService.isBookBorrowed(record1.getBookId()));
        assertFalse(borrowingService.isBookBorrowed(record2.getBookId()));
    }
}