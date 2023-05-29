package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Staff;

public class EditPersonnelController {

    @FXML
    private Label addressLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private ImageView imageField;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label phoneLabel;

    @FXML
    private ChoiceBox<String> staffChoiceBox;
    private String[] policlinicks = {"Cardiology", "Neurology", "Orthopedic Surgery", "Oncology", "Pediatrics", "Psychiatry", "Dermatology", "Ophthalmology", "Anesthesiology"};

    @FXML
    private TextField staffIdTextField;

    @FXML
    private TextField userNameTextField;

    public void updateFields(Staff personnel) {
        this.addressLabel.setText(personnel.getAddress());
        this.ageLabel.setText(String.valueOf(personnel.getAge()));
        this.emailLabel.setText(personnel.getEmail());
        this.genderLabel.setText(personnel.getGender().toString());
        this.imageField.setImage(new Image("ui/imgs/default_person.png"));
        this.nameSurnameLabel.setText(personnel.getName()+" "+personnel.getSurname());
        this.phoneLabel.setText(personnel.getPhone());
        staffChoiceBox.getItems().addAll(policlinicks);
    }

}
