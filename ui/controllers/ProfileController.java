package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Staff;

public class ProfileController{

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

    public void updateFields(String username) {
        Staff staff = Datasource.getInstance().queryProfile(username);
        nameField.setText(staff.getName());
        surnameField.setText(staff.getSurname());
    }
    
}
