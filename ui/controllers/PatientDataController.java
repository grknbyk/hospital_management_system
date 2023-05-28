package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Patient;

public class PatientDataController {
    private Patient patient;

    @FXML
    private Label ageLabel;

    @FXML
    private Label bloodLabel;

    @FXML
    private Label doctorNameLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label idLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priorityLabel;

    public void updateFields(Patient patient) {
        this.patient = patient;
        imageView.setImage(new Image("ui/imgs/default_person.png"));
        ageLabel.setText(patient.getName());
        bloodLabel.setText(patient.getBloodType().toString());
        doctorNameLabel.setText("doktor adi");
        genderLabel.setText(patient.getGender().toString());
        idLabel.setText(String.valueOf(patient.getId()));
        nameLabel.setText(patient.getName());
        priorityLabel.setText(patient.getPriority().toString());
    }
}
