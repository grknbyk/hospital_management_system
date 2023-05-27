package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Contact;
import model.Staff;
import model.enums.Gender;

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
    private Spinner<Integer> ageSpinner;
    
    @FXML
    private ChoiceBox<Gender> genderChoiceBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneTextField;

    public void showFields(Staff staff) {
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

    public void showEdit(Staff staff) {
        imageField.setImage(new Image("ui/imgs/default_person.png"));
        idField.setText(String.valueOf(staff.getId()));
        statusField.setText(staff.getStatus().toString());
        nameTextField.setText(staff.getName());
        surnameTextField.setText(staff.getSurname());
        
        phoneTextField.setText(staff.getContact().getPhone());
        emailTextField.setText(staff.getContact().getEmail());
        addressTextField.setText(staff.getContact().getAddress());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200);
        ageSpinner.setValueFactory(valueFactory);
        ageSpinner.getValueFactory().setValue(staff.getAge());

        genderChoiceBox.getItems().addAll(Gender.values());
        genderChoiceBox.setValue(staff.getGender());

    }

    public Staff returnEditedProfile() {
        return new Staff() {
            {
                setId(Integer.parseInt(idField.getText()));
                setName(nameTextField.getText());
                setSurname(surnameTextField.getText());
                setAge(ageSpinner.getValue());
                setGender(genderChoiceBox.getValue());
                Contact contact = new Contact();
                contact.setPhone(phoneTextField.getText());
                contact.setEmail(emailTextField.getText());
                contact.setAddress(addressTextField.getText());
                setContact(contact);
            }
        };
    }
    
}
