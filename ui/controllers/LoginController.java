package ui.controllers;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import database.Datasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private Button loginButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void login(ActionEvent event) throws IOException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

		String status = Datasource.getInstance().queryLogin(username, password); //kolaylık olsun diye elle girdim

		if (status == null) {
			System.out.println("Invalid username or password");
			loginErrorLabel.setVisible(true);
			return;
		} else {
			status = status.toLowerCase();
			switch (status) {
				case "manager":
					showManagerPanel(event,username);
					break;
				case "doctor":
					showDoctorPanel(event, username);
					break;
				case "nurse":
					showNursePanel(event, username);
					break;
				case "receptionist":
					showReceptionistPanel(event, username);
					break;
				case "pharmacist":
					showPharmacistPanel(event, username);
					break;
				default:
					// label.setText("You don't have access to this system");
					System.out.println("Invalid status: " + status);
					System.out.println("You don't have access to this system");
					break;
			}
		}

    }

    @FXML
    private void usernameKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            passwordTextField.requestFocus();
    }

    @FXML
    private void passwordKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            loginButton.fire();
    }

    private void showManagerPanel(ActionEvent event, String username) throws IOException {

        // Get the FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scene/ManagerScene.fxml"));
        // Load the second controller
        Parent root = loader.load();
        ManagerController managerController = loader.getController();
        managerController.setUsername(username);

        // Navigate to the second controller
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Hospital Management System - Manager");
        stage.setScene(scene);
        stage.show();

    }

    private void showDoctorPanel(ActionEvent event, String username) throws IOException {
        // Get the FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scene/DoctorScene.fxml"));
        // Load the second controller
        Parent root = loader.load();
        DoctorController doctorController = loader.getController();
        doctorController.setUsername(username);
        doctorController.loadPatients();
        // Navigate to the second controller
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Hospital Management System - Doctor");
        stage.setScene(scene);
        stage.show();
    }

    private void showNursePanel(ActionEvent event, String username) throws IOException {
        // Get the FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scene/NurseScene.fxml"));
        // Load the second controller
        Parent root = loader.load();
        NurseController nurseController = loader.getController();
        nurseController.setUsername(username);
        nurseController.loadPatients();
        // Navigate to the second controller
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Hospital Management System - Nurse");
        stage.setScene(scene);
        stage.show();
    }

    private void showReceptionistPanel(ActionEvent event, String username) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scene/RecepsionistScene.fxml"));
        Parent root = loader.load();
        ReceptionistController receptionistController = loader.getController();
        receptionistController.setUsername(username);


        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Hospital Management System - Receptionist");
        stage.setScene(scene);
        stage.show();
    }

    private void showPharmacistPanel(ActionEvent event, String username) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scene/PharmacistScene.fxml"));
        Parent root = loader.load();
        PharmacistController pharmacistController = loader.getController();
        pharmacistController.setUsername(username);
        pharmacistController.loadMedicine();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hospital Management System - Pharmacist");
        stage.setScene(scene);
        stage.show();
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
        Button closeButton = new Button("Close");
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
