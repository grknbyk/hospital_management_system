package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Staff;
import model.enums.Status;

import java.util.Optional;

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
    private Label phoneLabel;

    @FXML
    private ChoiceBox<Status> staffChoiceBox;

    @FXML
    private TextField staffIdTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;

    public void updateFields(Staff personnel) {
        this.addressLabel.setText(personnel.getAddress());
        this.ageLabel.setText(String.valueOf(personnel.getAge()));
        this.emailLabel.setText(personnel.getEmail());
        this.genderLabel.setText(personnel.getGender().toString());
        this.imageField.setImage(new Image("ui/imgs/default_person.png"));
        this.nameSurnameLabel.setText(personnel.getName()+" "+personnel.getSurname());
        this.phoneLabel.setText(personnel.getPhone());
        this.staffIdTextField.setText(String.valueOf(personnel.getId()));
        this.userNameTextField.setText(personnel.getUsername());
        this.staffChoiceBox.getItems().addAll(Status.values());
        this.staffChoiceBox.setValue(personnel.getStatus());
    }

    public boolean saveChanges(Staff personnel) {
        if(Datasource.getInstance().queryStaffProfile(Integer.parseInt(staffIdTextField.getText())) != null) {
            return alreadyTaken();
        }else {
            //update burda yapılacak
            return true;
        }
    }

    private boolean alreadyTaken() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Dialog");
        alert.setContentText("Staff ID already exists");
        alert.showAndWait();
        return false;
    }

}
