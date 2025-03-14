module com.maxiflexy.dreamdevs.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maxiflexy.dreamdevs.librarymanagementsystem to javafx.fxml;
    exports com.maxiflexy.dreamdevs.librarymanagementsystem;
}