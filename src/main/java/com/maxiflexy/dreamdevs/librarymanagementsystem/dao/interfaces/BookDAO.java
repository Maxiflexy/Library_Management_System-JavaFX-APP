package com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;

import java.util.List;

public interface BookDAO {

    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    List<Book> getAllBooks();
    Book getBookById(int bookId);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByGenre(String genre);
    boolean updateBookAvailability(int bookId, boolean increment);
}
