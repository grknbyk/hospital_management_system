package ui.controllers;

import java.io.IOException;

import database.Datasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
			loginErrorLabel.setDisable(false);
			return;
		} else {
			status = status.toLowerCase();
			switch (status) {
				case "manager":
					showManagerPanel(event,username);
					break;
				case "doctor":
					showDoctorPanel(event);
					break;
				case "nurse":
					showNursePanel(event);
					break;
				case "receptionist":
					showReceptionistPanel(event);
					break;
				case "pharmacist":
					showPharmacistPanel(event);
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


        // Navigate to the second controller
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Hospital Management System - Manager");
        stage.setScene(scene);
        stage.show();

    }

    private void showDoctorPanel(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("../scene/DoctorScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hospital Management System - Doctor");
        stage.setScene(scene);
        stage.show();
    }

    private void showNursePanel(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("../scene/NurseScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hospital Management System - Nurse");
        stage.setScene(scene);
        stage.show();
    }

    private void showReceptionistPanel(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("../scene/ReceptionistScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Hospital Management System - Receptionist");
        stage.setScene(scene);
        stage.show();
    }

    private void showPharmacistPanel(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("../scene/PharmacistScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Hospital Management System - Pharmacist");
        stage.setScene(scene);
        stage.show();
    }

}
