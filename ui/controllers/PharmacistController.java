package ui.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PharmacistController {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showProfileDialog() {
        System.out.println("Showing profile dialog");
    }

    public void logout() {
        System.out.println("Logging out");
    }


    public void showAboutDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Help");

        // Create labels and fields for email, phone, and website
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField("group1_oop@email.com");
        emailField.setEditable(false);
        emailField.setPrefWidth(200);
        emailField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                copyToClipboard(emailField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
            }
        });

        Label phoneLabel = new Label("Phone:");
        Label phoneField = new Label("+1 123-456-7890");

        Hyperlink websiteLink = new Hyperlink("Website: https://ceng.deu.edu.tr");
        websiteLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://ceng.deu.edu.tr"));
            } catch (IOException | URISyntaxException e) {
                // Handle any errors that occur while trying to open the link
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open website.");
            }
        });

        // Create a text area for additional instructions or information
        Label additionalInfoLabel = new Label();
        additionalInfoLabel.setText("This project is designed for OOP lecture in Dokuz Eylul University");

        // Create a button to close the dialog
        javafx.scene.control.Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialogStage.close());

        // Create a grid pane to hold the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(emailLabel, 0, 0);
        gridPane.add(emailField, 1, 0);
        gridPane.add(phoneLabel, 0, 1);
        gridPane.add(phoneField, 1, 1);
        gridPane.add(websiteLink, 0, 2, 2, 1);
        gridPane.add(additionalInfoLabel, 0, 3, 2, 1);
        gridPane.add(closeButton, 0, 4, 2, 1);

        // Set the scene for the dialog and show it
        Scene dialogScene = new Scene(gridPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
