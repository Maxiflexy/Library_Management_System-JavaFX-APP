package com.maxiflexy.dreamdevs.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.image.Image;

public class LibraryApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());

            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(scene);

            // Optional: Set application icon
            try {
                primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/library_icon.png"))));
            } catch (Exception e) {
                System.out.println("Icon not found or could not be loaded");
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}