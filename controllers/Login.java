package controllers;

import java.io.IOException;

import data.Datasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login {

	@FXML
	TextField usernameTextField;

	@FXML
	TextField passwordTextField;

	@FXML
	Button loginButton;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void login(ActionEvent event) throws IOException {

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

	public void showManagerPanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("../ui/ManagerScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showDoctorPanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("DoctorScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showNursePanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("NurseScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showReceptionistPanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("ReceptionistScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void showPharmacistPanel(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("PharmacistScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
