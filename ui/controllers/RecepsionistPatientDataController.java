package ui.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Patient;

public class RecepsionistPatientDataController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label patientNameLabel;

    @FXML
    private Label patientSurnameLabel;

    @FXML
    private Label patientGenderLabel;

    @FXML
    private Label patientAgeLabel;

    @FXML
    private Label patientBloodTypeLabel;

    @FXML
    private Label patientAppointmentLabel;

    @FXML
    private Label patientComplaintLabel;

    public void initializeFields(Patient patient) {
        imageView.setImage(new Image("ui/imgs/patient.png"));
        patientNameLabel.setText(patient.getName());
        patientSurnameLabel.setText(patient.getSurname());
        patientGenderLabel.setText(patient.getGender().toString());
        patientAgeLabel.setText(String.valueOf(patient.getAge()));
        patientBloodTypeLabel.setText(patient.getBloodType().toString());
        patientComplaintLabel.setText(patient.getComplaint());

        LocalDateTime localDateTime = patient.getAppointment();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        patientAppointmentLabel.setText(formattedDateTime);
    }
}
