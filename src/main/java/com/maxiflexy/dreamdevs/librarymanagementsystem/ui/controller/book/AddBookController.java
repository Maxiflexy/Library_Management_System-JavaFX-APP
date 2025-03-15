package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.book;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField copiesField;

    private BookService bookService;

    @FXML
    private void initialize() {
        bookService = new BookService();
    }

    @FXML
    private void handleSave() {
        if (validateInputs()) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            int availableCopies = Integer.parseInt(copiesField.getText().trim());

            Book book = new Book(title, author, genre, availableCopies);
            bookService.addBook(book);

            showAlert(Alert.AlertType.INFORMATION, "Book Added", "Book has been successfully added.");
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
