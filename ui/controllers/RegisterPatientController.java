package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import model.Patient;

import java.time.LocalDate;

public class RegisterPatientController {

    @FXML
    private TextArea complaintTextArea;


    @FXML
    private TextArea emergencyTextArea;

    @FXML
    private TextArea priorityTextArea;

    @FXML
    private ChoiceBox<String> policlinicBox;
    private String[] policlinicks = {"Cardiology", "Neurology", "Orthopedic Surgery", "Oncology", "Pediatrics", "Psychiatry", "Dermatology", "Ophthalmology", "Anesthesiology", "Cardiology"};

    @FXML
    private ChoiceBox<String> serviceBox;
    private String[] serviceAreas = {"Intensive Care Unit","Emergency Department", "Operating Room", "Medical-Surgical Ward"};

    @FXML
    private DatePicker datePicker;


    public void updateFields(Patient patient) {
        complaintTextArea.setText(patient.getComplaint());
        complaintTextArea.setEditable(false);

        emergencyTextArea.setText(patient.getEmergencyState().toString());
        emergencyTextArea.setEditable(false);

        priorityTextArea.setText(patient.getPriority().toString());
        priorityTextArea.setEditable(false);

        serviceBox.getItems().addAll(serviceAreas);
        policlinicBox.getItems().addAll(policlinicks);
    }
}
