package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
        idField.setText(String.valueOf(staff.getId()));
        emailField.setText(staff.getContact().getEmail());
        phoneField.setText(staff.getContact().getPhone());
        statusField.setText(staff.getStatus().toString());
        addressField.setText(staff.getContact().getAddress());
        ageField.setText(String.valueOf(staff.getAge()));
        genderField.setText(staff.getGender().toString());
        imageField.setImage(new Image("ui/imgs/default_person.png"));
    }
    
}
