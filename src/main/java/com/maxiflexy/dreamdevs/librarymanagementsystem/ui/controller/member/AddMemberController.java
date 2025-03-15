package com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.member;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import com.maxiflexy.dreamdevs.librarymanagementsystem.service.MemberService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMemberController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    private MemberService memberService;

    @FXML
    private void initialize() {
        memberService = new MemberService();
    }

    @FXML
    private void handleSave() {
        if (validateInputs()) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            Member member = new Member(name, email, phone);
            memberService.addMember(member);

            showAlert(Alert.AlertType.INFORMATION, "Member Added", "Member has been successfully added.");
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Email cannot be empty.");
            return false;
        }

        if (phoneField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone cannot be empty.");
            return false;
        }

        // Additional email validation
        String email = emailField.getText().trim();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.");
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
        ((Stage) nameField.getScene().getWindow()).close();
    }
}