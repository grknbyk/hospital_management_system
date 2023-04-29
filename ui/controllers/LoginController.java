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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordTextField;

	@FXML
	private Button loginButton;

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private void login(ActionEvent event) throws IOException {

		String username = usernameTextField.getText();
		String password = passwordTextField.getText();

		String status = Datasource.getInstance().queryLogin(username, password);

		if (status == null) {
			System.out.println("Invalid username or password"); // label.setText("Invalid username or password");
			return;
		} else {
			status = status.toLowerCase();
			switch (status) {
				case "manager":
					showManagerPanel(event);
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

	private void showManagerPanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("../scene/ManagerScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
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
