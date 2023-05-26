package ui.controllers;

import database.Datasource;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Contact;
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

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField genderTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneTextField;

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

    public void updateEdit(String username) {
        Staff staff = Datasource.getInstance().queryProfile(username);
        imageField.setImage(new Image("ui/imgs/default_person.png"));
        idField.setText(String.valueOf(staff.getId()));
        statusField.setText(staff.getStatus().toString());

        nameTextField.setText(staff.getName());
        surnameTextField.setText(staff.getSurname());
        genderTextField.setText(staff.getGender().toString());
        ageTextField.setText(String.valueOf(staff.getAge()));
        phoneTextField.setText(staff.getContact().getPhone());
        emailTextField.setText(staff.getContact().getEmail());
        addressTextField.setText(staff.getContact().getAddress());

    }

    public void editFields(String username) {
        Staff staff = Datasource.getInstance().queryProfile(username);

        imageField.setImage(new Image("ui/imgs/default_person.png"));
        idField.setText(String.valueOf(staff.getId()));
        statusField.setText(staff.getStatus().toString());

        staff.setName(nameTextField.getText()); // database de güncelleme olmuyor

//        nameField.setText(staff.getName());
//        surnameField.setText(staff.getSurname());
//        idField.setText(String.valueOf(staff.getId()));
//        emailField.setText(staff.getContact().getEmail());
//        phoneField.setText(staff.getContact().getPhone());
//        statusField.setText(staff.getStatus().toString());
//        addressField.setText(staff.getContact().getAddress());
//        ageField.setText(String.valueOf(staff.getAge()));
//        genderField.setText(staff.getGender().toString());
//        imageField.setImage(new Image("ui/imgs/default_person.png"));

    }
    
}
