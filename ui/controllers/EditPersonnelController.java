package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;
import model.enums.*;

import java.util.Optional;

import javax.print.Doc;

public class EditPersonnelController {

    private String username;
    private String password;

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
    private Label expertiseLabel;

    @FXML
    private Label workingAreaLabel;

    @FXML
    private ChoiceBox<String> choiceBoxAdditional;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;

    public void initialize() {
        this.staffChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == Status.DOCTOR) {
                workingAreaLabel.setVisible(false);
                expertiseLabel.setVisible(true);
                choiceBoxAdditional.getItems().clear();
                for (model.enums.Expertise expertise : model.enums.Expertise.values()) {
                    choiceBoxAdditional.getItems().add(expertise.toString());
                }
                choiceBoxAdditional.setVisible(true);
            } else if(newValue == Status.NURSE){
                expertiseLabel.setVisible(false);
                workingAreaLabel.setVisible(true);
                choiceBoxAdditional.getItems().clear();
                for (model.enums.WorkingArea workingArea : model.enums.WorkingArea.values()) {
                    choiceBoxAdditional.getItems().add(workingArea.toString());
                }
                choiceBoxAdditional.setVisible(true);
            }else{
                expertiseLabel.setVisible(false);
                workingAreaLabel.setVisible(false);
                choiceBoxAdditional.setVisible(false);
            }
        });
    }

    public void updateFields(Staff personnel) {
        this.username = personnel.getUsername();
        this.password = personnel.getPassword();
        this.addressLabel.setText(personnel.getAddress());
        this.ageLabel.setText(String.valueOf(personnel.getAge()));
        this.emailLabel.setText(personnel.getEmail());
        this.genderLabel.setText(personnel.getGender().toString());
        this.imageField.setImage(new Image("ui/imgs/default_person.png"));
        this.nameSurnameLabel.setText(personnel.getName()+" "+personnel.getSurname());
        this.phoneLabel.setText(personnel.getPhone());
        this.userNameTextField.setText(personnel.getUsername());
        this.passwordTextField.setText(personnel.getPassword());
        this.staffChoiceBox.getItems().addAll(Status.values());
        this.staffChoiceBox.setValue(personnel.getStatus());

        if(personnel instanceof Doctor){
            this.choiceBoxAdditional.setValue(((Doctor) personnel).getExpertise());
        }
        if(personnel instanceof model.Nurse){
            this.choiceBoxAdditional.setValue(((model.Nurse) personnel).getWorkingArea());
        }

        
    }

    public boolean saveChanges(Staff personnel) {
        Staff staff = personnel;
        staff.setUsername(userNameTextField.getText());
        staff.setPassword(passwordTextField.getText());
        
        String result = Datasource.getInstance().updateStaffUser(staff);
        if(result.equals("Staff updated successfully") || result.equals("Couldn't update the staff completely"))
            return true;
        else
            return alreadyTaken();
           
    }

    private boolean alreadyTaken() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Dialog");
        alert.setContentText("Staff ID already exists");
        alert.showAndWait();
        return false;
    }

    public void usernameResetButton(){
        this.userNameTextField.setText(this.username);
    }

    public void passwordResetButton(){
        this.passwordTextField.setText(this.password);
    }

}
