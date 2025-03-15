package com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl;

import com.maxiflexy.dreamdevs.librarymanagementsystem.config.DatabaseConnection;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BookDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    private static final Logger logger = new Logger();

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, genre, available_copies) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setBookId(generatedKeys.getInt(1));
                    logger.log("Book added: " + book.getTitle());
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.log("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, available_copies = ? WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());
            stmt.setInt(5, book.getBookId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.log("Book updated: " + book.getTitle());
            } else {
                logger.log("Book update failed: No book found with ID " + book.getBookId());
            }
        } catch (SQLException e) {
            logger.log("Error updating book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.log("Book deleted with ID: " + bookId);
            } else {
                logger.log("Book deletion failed: No book found with ID " + bookId);
            }
        } catch (SQLException e) {
            logger.log("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setAvailableCopies(rs.getInt("available_copies"));

                books.add(book);
            }
        } catch (SQLException e) {
            logger.log("Error retrieving all books: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setAvailableCopies(rs.getInt("available_copies"));

                    return book;
                }
            }
        } catch (SQLException e) {
            logger.log("Error retrieving book by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setAvailableCopies(rs.getInt("available_copies"));

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            logger.log("Error searching books by title: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + author + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setAvailableCopies(rs.getInt("available_copies"));

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            logger.log("Error searching books by author: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(genre) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + genre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setAvailableCopies(rs.getInt("available_copies"));

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            logger.log("Error searching books by genre: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public boolean updateBookAvailability(int bookId, boolean increment) {
        String sql = increment ?
                "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?" :
                "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                String action = increment ? "returned" : "borrowed";
                logger.log("Book with ID " + bookId + " " + action + ". Available copies updated.");
                return true;
            } else {
                if (!increment) {
                    logger.log("Cannot borrow book with ID " + bookId + ": No available copies.");
                } else {
                    logger.log("Failed to update availability for book with ID " + bookId);
                }
                return false;
            }
        } catch (SQLException e) {
            logger.log("Error updating book availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}