module com.maxiflexy.dreamdevs.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    // Open all controller packages to JavaFX FXML
    opens com.maxiflexy.dreamdevs.librarymanagementsystem to javafx.fxml;
    opens com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller to javafx.fxml;
    opens com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.book to javafx.fxml;
    opens com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.member to javafx.fxml;
    opens com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.borrowing to javafx.fxml;

    // Export packages that need to be accessed by other modules
    exports com.maxiflexy.dreamdevs.librarymanagementsystem;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.model;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.service;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.book;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.member;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem.ui.controller.borrowing;
}