package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.member;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.MemberService;
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

public class MemberListController {

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private TableColumn<Member, Integer> idColumn;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TableColumn<Member, String> phoneColumn;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    private MemberService memberService;
    private final ObservableList<Member> memberData = FXCollections.observableArrayList();
    private List<Member> filteredMembers;

    private static final int ROWS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        memberService = new MemberService();

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMemberId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));

        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMembers();
        });

        // Initialize pagination
        loadMembers();
    }

    private void loadMembers() {
        filteredMembers = memberService.getAllMembers();
        setupPagination();
    }

    private void setupPagination() {
        int totalPages = (filteredMembers.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        pagination.setPageCount(totalPages);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredMembers.size());

        memberData.clear();
        memberData.addAll(filteredMembers.subList(fromIndex, toIndex));
        memberTable.setItems(memberData);

        return memberTable;
    }

    private void searchMembers() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            filteredMembers = memberService.getAllMembers();
        } else {
            filteredMembers = memberService.getAllMembers().stream()
                    .filter(member ->
                            member.getName().toLowerCase().contains(searchText) ||
                                    member.getEmail().toLowerCase().contains(searchText) ||
                                    member.getPhone().toLowerCase().contains(searchText))
                    .toList();
        }

        setupPagination();
    }

    @FXML
    private void handleAddMember() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/member/add_member.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Member");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh members after dialog closes
            loadMembers();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not load the Add Member dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a member to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/member/edit_member.fxml"));
            Parent root = loader.load();

            EditMemberController controller = loader.getController();
            controller.setMember(selectedMember);

            Stage stage = new Stage();
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh members after dialog closes
            loadMembers();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not load the Edit Member dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a member to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Member");
        confirmAlert.setContentText("Are you sure you want to delete the member: " + selectedMember.getName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            memberService.deleteMember(selectedMember.getMemberId());
            loadMembers();
            showAlert(Alert.AlertType.INFORMATION, "Member Deleted", "Member has been successfully deleted.");
        }
    }

    @FXML
    private void handleExportToCSV() {
        memberService.exportMembersToCSV();
        showAlert(Alert.AlertType.INFORMATION, "Export Successful", "All members have been exported to CSV.");
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
