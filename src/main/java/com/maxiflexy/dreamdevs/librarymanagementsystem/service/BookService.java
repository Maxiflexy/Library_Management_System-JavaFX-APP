package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl.BookDAOImpl;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BookDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.CSVExporter;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final BookDAO bookDAO;
    private final Logger logger;
    private final CSVExporter csvExporter;

    // In-memory cache for books
    private List<Book> booksCache;

    // Default constructor using BookDAOImpl
    public BookService() {
        this(new BookDAOImpl());
    }

    // Constructor for dependency injection (useful for testing)
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        this.logger = new Logger();
        this.csvExporter = new CSVExporter();
        this.booksCache = new ArrayList<>();
        refreshCache();
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);
        refreshCache();
    }

    public void updateBook(Book book) {
        bookDAO.updateBook(book);
        refreshCache();
    }

    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
        refreshCache();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(booksCache); // Return copy of in-memory collection
    }

    // Added pagination support
    public List<Book> getBooksByPage(int page, int pageSize) {
        int fromIndex = page * pageSize;
        if (fromIndex >= booksCache.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(fromIndex + pageSize, booksCache.size());
        return new ArrayList<>(booksCache.subList(fromIndex, toIndex));
    }

    public int getTotalBooks() {
        return booksCache.size();
    }

    public Book getBookById(int bookId) {
        return booksCache.stream()
                .filter(book -> book.getBookId() == bookId)
                .findFirst()
                .orElseGet(() -> bookDAO.getBookById(bookId));
    }

    public List<Book> searchBooksByTitle(String title) {
        return booksCache.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        return booksCache.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByGenre(String genre) {
        return booksCache.stream()
                .filter(book -> book.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> sortBooksByTitle() {
        List<Book> sortedBooks = new ArrayList<>(booksCache);
        Collections.sort(sortedBooks); // Using Comparable implementation
        return sortedBooks;
    }

    public List<Book> sortBooksByGenre() {
        List<Book> sortedBooks = new ArrayList<>(booksCache);
        sortedBooks.sort(Comparator.comparing(Book::getGenre)); // Using Comparator
        return sortedBooks;
    }

    public boolean updateBookAvailability(int bookId, boolean increment) {
        boolean success = bookDAO.updateBookAvailability(bookId, increment);
        if (success) {
            refreshCache();
        }
        return success;
    }

    public void exportBooksToCSV() {
        csvExporter.exportBooksToCSV(booksCache);
    }

    public void refreshCache() {
        booksCache = bookDAO.getAllBooks();
    }
}