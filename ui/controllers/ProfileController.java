package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Staff;

public class ProfileController {
    String username;

    @FXML
    private Label ageField;

    @FXML
    private Label genderField;

    @FXML
    private Label surnameField;

    @FXML
    private Label addressField;

    @FXML
    private Label emailField;

    @FXML
    private ImageView imageField;

    @FXML
    private Label nameField;

    @FXML
    private Label idField;


    @FXML
    private Label phoneField;

    @FXML
    private Label statusField;

    public void initialize() {
        System.out.println(username);
        Staff staff = Datasource.getInstance().queryProfile(getUsername());
        idField.setText(String.valueOf(staff.getId()));
        //statusField.setText(staff.getStatus());
        nameField.setText(staff.getName());
        surnameField.setText(staff.getSurname());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
