package ui.controllers;

import java.beans.Expression;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;
import model.enums.*;

public class UserController {
    @FXML
    private ImageView imageField;

    @FXML
    private Spinner<Integer> ageSpinner;

    @FXML
    private ChoiceBox<Gender> genderChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private ChoiceBox<Status> statusChoiceBox;

    @FXML
    private ChoiceBox<String> statusChoiceBox2;

    @FXML
    private Label workingAreaLabel;

    @FXML
    private Label expertiseLabel;

    public void initialize() {
        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        imageField.setImage(menuImg);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(18, 100);
        ageSpinner.setValueFactory(valueFactory);
        ageSpinner.getValueFactory().setValue(18);

        genderChoiceBox.getItems().addAll(Gender.values());
        genderChoiceBox.setValue(Gender.MALE);

        workingAreaLabel.setVisible(false);
        expertiseLabel.setVisible(false);
        statusChoiceBox2.setVisible(false);
        statusChoiceBox.getItems().addAll(Status.values());
        statusChoiceBox.setValue(Status.DOCTOR);
        
        statusChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == Status.DOCTOR) {
                workingAreaLabel.setVisible(false);
                expertiseLabel.setVisible(true);
                statusChoiceBox2.getItems().clear();
                for (Expertise expertise : Expertise.values()) {
                    statusChoiceBox2.getItems().add(expertise.toString());
                }
                statusChoiceBox2.setVisible(true);
            } else if(newValue == Status.NURSE){
                expertiseLabel.setVisible(false);
                workingAreaLabel.setVisible(true);
                statusChoiceBox2.getItems().clear();
                for (WorkingArea workingArea : WorkingArea.values()) {
                    statusChoiceBox2.getItems().add(workingArea.toString());
                }
                statusChoiceBox2.setVisible(true);
            } else{
                workingAreaLabel.setVisible(false);
                expertiseLabel.setVisible(false);
                statusChoiceBox2.setVisible(false);
            }
        });
    }

    public void fillTheFields(Staff staff) {
        nameTextField.setText(staff.getName());
        surnameTextField.setText(staff.getSurname());
        phoneTextField.setText(staff.getContact().getPhone());
        emailTextField.setText(staff.getContact().getEmail());
        addressTextField.setText(staff.getContact().getAddress());
        ageSpinner.getValueFactory().setValue(staff.getAge());
        genderChoiceBox.setValue(staff.getGender());
        statusChoiceBox.setValue(staff.getStatus());
    }

    public Staff returnStaff(){
        return null;
    }

}
