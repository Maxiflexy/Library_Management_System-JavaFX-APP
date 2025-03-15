package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.book;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BookService;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.List;

public class BookListController {

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, Integer> copiesColumn;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchTypeComboBox;

    private BookService bookService;
    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    private List<Book> filteredBooks;

    private static final int ROWS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        bookService = new BookService();

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        copiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());

        // Set up search type combo box
        searchTypeComboBox.setItems(FXCollections.observableArrayList("Title", "Author", "Genre"));
        searchTypeComboBox.getSelectionModel().select(0);

        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBooks();
        });

        // Initialize pagination
        loadBooks();
    }

    private void loadBooks() {
        filteredBooks = bookService.getAllBooks();
        System.out.println("Total books loaded: " + filteredBooks.size());
        setupPagination();
    }

    private void setupPagination() {
        int totalBooks = filteredBooks.size();
        int totalPages = (totalBooks + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;

        System.out.println("Setting up pagination: " + totalBooks + " books, " + totalPages + " pages");

        //pagination.setPageCount(totalPages);
        pagination.setPageCount(Math.max(1, totalPages));
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        System.out.println("Creating page: " + pageIndex);

        int fromIndex = pageIndex * ROWS_PER_PAGE;
        if (fromIndex >= filteredBooks.size()) {
            System.out.println("From index out of bounds: " + fromIndex);
            bookTable.setItems(FXCollections.observableArrayList());
            return bookTable;
        }

        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredBooks.size());
        System.out.println("Displaying books from " + fromIndex + " to " + toIndex);

        // Debug - print the actual books on this page
        for (int i = fromIndex; i < toIndex; i++) {
            Book book = filteredBooks.get(i);
            System.out.println("  Book: " + book.getBookId() + " - " + book.getTitle());
        }

        // Create a brand new ObservableList for each page
        ObservableList<Book> pageData = FXCollections.observableArrayList(
                filteredBooks.subList(fromIndex, toIndex)
        );

        // Set the items and explicitly refresh
        bookTable.setItems(pageData);
        bookTable.refresh();

        return bookTable;
    }


    private void searchBooks() {
        String searchText = searchField.getText().trim();
        String searchType = searchTypeComboBox.getValue();

        if (searchText.isEmpty()) {
            filteredBooks = bookService.getAllBooks();
        } else {
            switch (searchType) {
                case "Title":
                    filteredBooks = bookService.searchBooksByTitle(searchText);
                    break;
                case "Author":
                    filteredBooks = bookService.searchBooksByAuthor(searchText);
                    break;
                case "Genre":
                    filteredBooks = bookService.searchBooksByGenre(searchText);
                    break;
                default:
                    filteredBooks = bookService.searchBooksByTitle(searchText);
            }
        }

        setupPagination();
    }

    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/book/add_book.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh books after dialog closes
            loadBooks();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not load the Add Book dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/book/edit_book.fxml"));
            Parent root = loader.load();

            EditBookController controller = loader.getController();
            controller.setBook(selectedBook);

            Stage stage = new Stage();
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh books after dialog closes
            loadBooks();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not load the Edit Book dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Book");
        confirmAlert.setContentText("Are you sure you want to delete the book: " + selectedBook.getTitle() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            bookService.deleteBook(selectedBook.getBookId());
            loadBooks();
            showAlert(Alert.AlertType.INFORMATION, "Book Deleted", "Book has been successfully deleted.");
        }
    }

    @FXML
    private void handleExportToCSV() {
        bookService.exportBooksToCSV();
        showAlert(Alert.AlertType.INFORMATION, "Export Successful", "All books have been exported to CSV.");
    }

    @FXML
    private void handleSortByTitle() {
        filteredBooks = bookService.sortBooksByTitle();
        setupPagination();
    }

    @FXML
    private void handleSortByGenre() {
        filteredBooks = bookService.sortBooksByGenre();
        setupPagination();
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
