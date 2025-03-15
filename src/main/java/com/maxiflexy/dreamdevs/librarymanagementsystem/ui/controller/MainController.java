package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private void initialize() {
        // Set default view to book list
        loadBookListView();
    }

    @FXML
    private void loadBookListView() {
        loadView("/view/book_list.fxml");
    }

    @FXML
    private void loadMemberListView() {
        loadView("/view/member/member_list.fxml");
    }

    @FXML
    private void loadBorrowingListView() {
        loadView("/view/borrowing/borrowing_list.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error loading view", "Could not load the requested view: " + fxmlPath);
        }
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Library Management System");
        alert.setHeaderText("Library Management System");
        alert.setContentText("Version 1.0\n\nA comprehensive library management application.\n\n" +
                "Features:\n• Book management\n• Member management\n• Borrowing management\n• Data export");
        alert.showAndWait();
    }

    @FXML
    private void exitApplication() {
        Platform.exit();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
