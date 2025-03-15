package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.book;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditBookController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField copiesField;

    private BookService bookService;
    private Book book;

    @FXML
    private void initialize() {
        bookService = new BookService();
    }

    public void setBook(Book book) {
        this.book = book;
        populateFields();
    }

    private void populateFields() {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreField.setText(book.getGenre());
        copiesField.setText(String.valueOf(book.getAvailableCopies()));
    }

    @FXML
    private void handleSave() {
        if (validateInputs()) {
            book.setTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            book.setGenre(genreField.getText().trim());
            book.setAvailableCopies(Integer.parseInt(copiesField.getText().trim()));

            bookService.updateBook(book);

            showAlert(Alert.AlertType.INFORMATION, "Book Updated", "Book has been successfully updated.");
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Title cannot be empty.");
            return false;
        }

        if (authorField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Author cannot be empty.");
            return false;
        }

        if (genreField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Genre cannot be empty.");
            return false;
        }

        try {
            int copies = Integer.parseInt(copiesField.getText().trim());
            if (copies < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Available copies must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Available copies must be a valid number.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        ((Stage) titleField.getScene().getWindow()).close();
    }
}