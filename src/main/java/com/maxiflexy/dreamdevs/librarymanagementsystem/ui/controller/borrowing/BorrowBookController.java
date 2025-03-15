package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.borrowing;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BookService;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.BorrowingService;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.MemberService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class BorrowBookController {

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> bookIdColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> availableColumn;

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private TableColumn<Member, Integer> memberIdColumn;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TextField bookSearchField;

    @FXML
    private TextField memberSearchField;

    private BookService bookService;
    private MemberService memberService;
    private BorrowingService borrowingService;

    private final ObservableList<Book> bookData = FXCollections.observableArrayList();
    private final ObservableList<Member> memberData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        bookService = new BookService();
        memberService = new MemberService();
        borrowingService = new BorrowingService(bookService);

        // Setup book table columns
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        availableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());

        // Setup member table columns
        memberIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMemberId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        // Load available books and members
        loadAvailableBooks();
        loadMembers();

        // Setup search listeners
        bookSearchField.textProperty().addListener((observable, oldValue, newValue) -> searchBooks(newValue));

        memberSearchField.textProperty().addListener((observable, oldValue, newValue) -> searchMembers(newValue));
    }

    private void loadAvailableBooks() {
        List<Book> availableBooks = bookService.getAllBooks().stream()
                .filter(book -> book.getAvailableCopies() > 0)
                .toList();

        bookData.clear();
        bookData.addAll(availableBooks);
        bookTable.setItems(bookData);
    }

    private void loadMembers() {
        List<Member> members = memberService.getAllMembers();

        memberData.clear();
        memberData.addAll(members);
        memberTable.setItems(memberData);
    }

    private void searchBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadAvailableBooks();
            return;
        }

        searchText = searchText.toLowerCase().trim();

        String finalSearchText = searchText;
        List<Book> filteredBooks = bookService.getAllBooks().stream()
                .filter(book -> book.getAvailableCopies() > 0 &&
                        (book.getTitle().toLowerCase().contains(finalSearchText) ||
                                book.getAuthor().toLowerCase().contains(finalSearchText)))
                .toList();

        bookData.clear();
        bookData.addAll(filteredBooks);
    }

    private void searchMembers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadMembers();
            return;
        }

        searchText = searchText.toLowerCase().trim();

        String finalSearchText = searchText;
        List<Member> filteredMembers = memberService.getAllMembers().stream()
                .filter(member -> member.getName().toLowerCase().contains(finalSearchText) ||
                        member.getEmail().toLowerCase().contains(finalSearchText))
                .toList();

        memberData.clear();
        memberData.addAll(filteredMembers);
    }

    @FXML
    private void handleBorrow() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to borrow.");
            return;
        }

        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Member Selected", "Please select a member who will borrow the book.");
            return;
        }

        boolean success = borrowingService.borrowBook(selectedBook.getBookId(), selectedMember.getMemberId());

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed",
                    selectedMember.getName() + " has successfully borrowed \"" + selectedBook.getTitle() + "\".");
            closeDialog();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to borrow the book. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        ((Stage) bookTable.getScene().getWindow()).close();
    }
}
