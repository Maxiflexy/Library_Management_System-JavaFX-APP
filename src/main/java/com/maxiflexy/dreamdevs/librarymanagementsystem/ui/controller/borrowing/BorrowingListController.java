package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.borrowing;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.BorrowingRecord;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BookService;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BorrowingService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class BorrowingListController {

    @FXML
    private TableView<BorrowingRecord> borrowingTable;

    @FXML
    private TableColumn<BorrowingRecord, Integer> idColumn;

    @FXML
    private TableColumn<BorrowingRecord, String> bookTitleColumn;

    @FXML
    private TableColumn<BorrowingRecord, String> memberNameColumn;

    @FXML
    private TableColumn<BorrowingRecord, Date> borrowDateColumn;

    @FXML
    private TableColumn<BorrowingRecord, Date> returnDateColumn;

    @FXML
    private Pagination pagination;

    @FXML
    private RadioButton allRecordsRadio;

    @FXML
    private RadioButton activeRecordsRadio;

    private BookService bookService;
    private BorrowingService borrowingService;
    private ObservableList<BorrowingRecord> borrowingData = FXCollections.observableArrayList();
    private List<BorrowingRecord> filteredRecords;

    private static final int ROWS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        bookService = new BookService();
        borrowingService = new BorrowingService(bookService);

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRecordId()).asObject());
        bookTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookTitle()));
        memberNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMemberName()));
        borrowDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnDate()));

        // Add status column formatter
        returnDateColumn.setCellFactory(column -> new TableCell<BorrowingRecord, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);

                // Clear previous content first
                setText(null);
                setStyle("");

                // Only set content if the cell is not empty and the row item exists
                if (!empty && getTableRow() != null && getTableRow().getItem() != null) {
                    if (item == null) {
                        setText("Not Returned");
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setText(item.toString());
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });

        // Setup radio buttons for filtering
        ToggleGroup filterGroup = new ToggleGroup();
        allRecordsRadio.setToggleGroup(filterGroup);
        activeRecordsRadio.setToggleGroup(filterGroup);
        allRecordsRadio.setSelected(true);

        filterGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            loadBorrowingRecords();
        });

        // Initialize pagination
        loadBorrowingRecords();
    }

    private void loadBorrowingRecords() {
        if (activeRecordsRadio.isSelected()) {
            filteredRecords = borrowingService.getActiveBorrowingRecords();
        } else {
            filteredRecords = borrowingService.getAllBorrowingRecords();
        }
        setupPagination();
    }

    private void setupPagination() {
        int totalPages = (filteredRecords.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        pagination.setPageCount(totalPages > 0 ? totalPages : 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredRecords.size());

        borrowingData.clear();

        if (fromIndex < filteredRecords.size()) {
            borrowingData.addAll(filteredRecords.subList(fromIndex, toIndex));
        }

        borrowingTable.setItems(borrowingData);

        return borrowingTable;
    }

    @FXML
    private void handleBorrowBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/borrowing/borrow_book.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Borrow Book");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh borrowing records after dialog closes
            loadBorrowingRecords();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not load the Borrow Book dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnBook() {
        BorrowingRecord selectedRecord = borrowingTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a borrowing record to process a return.");
            return;
        }

        if (selectedRecord.getReturnDate() != null) {
            showAlert(Alert.AlertType.WARNING, "Already Returned", "This book has already been returned.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText("Return Book");
        confirmAlert.setContentText("Are you sure you want to return the book: " + selectedRecord.getBookTitle() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = borrowingService.returnBook(selectedRecord.getRecordId());

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Book Returned", "Book has been successfully returned.");
                loadBorrowingRecords();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to return the book. Please try again.");
            }
        }
    }

    private void showErrorAlert(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}