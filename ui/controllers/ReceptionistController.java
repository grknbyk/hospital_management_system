package ui.controllers;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import model.Patient;
import model.Staff;

public class ReceptionistController {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private BorderPane receptionistPanel;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private TableView<Patient> patientTableView;
    private ObservableList<Patient> patients;

    @FXML
    private MenuButton options;

    public void initialize() {
        javafx.scene.image.Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);
    }

    public void loadPatients(){
        //fill the table
        patients = FXCollections.observableArrayList(Datasource.getInstance().queryPatientsNullStaff());
        patientTableView.setItems(patients);
    }

    public void denyPatient() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if(selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Select a patient to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // hasta silme
        }
    }

    public void registerPatient() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if(selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Select a patient");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(receptionistPanel.getScene().getWindow());
        dialog.setTitle("Register Patient");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/RegisterPatient.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            RegisterPatientController registerPatientController = fxmlLoader.getController();
            registerPatientController.updateFields(selectedPatient);
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog.close();
        });

        ButtonType applyButton = new ButtonType("Apply");
        ButtonType cancelButton = new ButtonType("Cancel");

        dialog.getDialogPane().getButtonTypes().addAll(applyButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == applyButton) {
            applyButtonFunction(selectedPatient);
        }else if(result.isPresent() && result.get() == cancelButton){
            dialog.close();
        }
    }

    private void applyButtonFunction(Patient patient){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to save the changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //update patient here
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            registerPatient();
        }
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, receptionistPanel).showProfileView();
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }

    public void showAboutDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Help");

        // Create labels and fields for email, phone, and website
        Label emailLabel = new Label("Email:");
        Hyperlink emailField = new Hyperlink("group1_oop@email.com");
        emailField.setPrefWidth(200);
        emailField.setOnMouseClicked(event -> {
            copyToClipboard(emailField.getText());
            showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
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
