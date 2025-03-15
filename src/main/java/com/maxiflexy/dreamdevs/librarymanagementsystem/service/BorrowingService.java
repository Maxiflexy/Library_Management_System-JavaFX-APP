package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl.BorrowingDAOImpl;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BorrowingDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.BorrowingRecord;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowingService {
    private final BorrowingDAO borrowingDAO;
    private final BookService bookService;
    private final Logger logger;

    // HashMap to track borrowed books (key: bookId, value: memberId)
    private final Map<Integer, Integer> borrowedBooks;

    // Default constructor
    public BorrowingService(BookService bookService) {
        this(new BorrowingDAOImpl(), bookService);
    }

    // Constructor for dependency injection (useful for testing)
    public BorrowingService(BorrowingDAO borrowingDAO, BookService bookService) {
        this.borrowingDAO = borrowingDAO;
        this.bookService = bookService;
        this.logger = new Logger();
        this.borrowedBooks = new HashMap<>();
        loadActiveBorrowings();
    }

    public boolean borrowBook(int bookId, int memberId) {
        // Check if book is available
        if (bookService.updateBookAvailability(bookId, false)) {
            // Create borrowing record
            BorrowingRecord record = new BorrowingRecord(
                    bookId,
                    memberId,
                    Date.valueOf(LocalDate.now())
            );

            borrowingDAO.addBorrowingRecord(record);
            borrowedBooks.put(bookId, memberId);
            return true;
        }

        return false;
    }

    public boolean returnBook(int recordId) {
        BorrowingRecord record = borrowingDAO.getBorrowingRecordById(recordId);

        if (record != null && record.getReturnDate() == null) {
            record.setReturnDate(Date.valueOf(LocalDate.now()));
            borrowingDAO.updateReturnDate(recordId, record.getReturnDate());

            // Increment available copies
            bookService.updateBookAvailability(record.getBookId(), true);

            // Remove from tracking map
            borrowedBooks.remove(record.getBookId());

            return true;
        }

        logger.log("Return book failed: Record not found or already returned");
        return false;
    }

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingDAO.getAllBorrowingRecords();
    }

    // Added pagination support
    public List<BorrowingRecord> getBorrowingRecordsByPage(int page, int pageSize) {
        List<BorrowingRecord> allRecords = borrowingDAO.getAllBorrowingRecords();

        int fromIndex = page * pageSize;
        if (fromIndex >= allRecords.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(fromIndex + pageSize, allRecords.size());
        return new ArrayList<>(allRecords.subList(fromIndex, toIndex));
    }

    public int getTotalBorrowingRecords() {
        return borrowingDAO.getAllBorrowingRecords().size();
    }

    public List<BorrowingRecord> getActiveBorrowingRecords() {
        return borrowingDAO.getActiveBorrowingRecords();
    }

    public List<BorrowingRecord> getBorrowingRecordsByMember(int memberId) {
        return borrowingDAO.getBorrowingRecordsByMember(memberId);
    }

    public boolean isBookBorrowed(int bookId) {
        return borrowedBooks.containsKey(bookId);
    }

    public Map<Integer, Integer> getBorrowedBooksMap() {
        return new HashMap<>(borrowedBooks);
    }

    private void loadActiveBorrowings() {
        List<BorrowingRecord> activeRecords = borrowingDAO.getActiveBorrowingRecords();

        for (BorrowingRecord record : activeRecords) {
            borrowedBooks.put(record.getBookId(), record.getMemberId());
        }
    }
}