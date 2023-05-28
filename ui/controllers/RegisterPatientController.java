package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.Patient;

public class RegisterPatientController {

    @FXML
    private TextArea complaintTextArea;

    @FXML
    private TextArea emergencyTextArea;

    @FXML
    private TextArea priorityTextArea;

    public void updateFields(Patient patient) {
        complaintTextArea.setText(patient.getComplaint());
        complaintTextArea.setEditable(false);

        emergencyTextArea.setText(patient.getEmergencyState().toString());
        emergencyTextArea.setEditable(false);

        priorityTextArea.setText(patient.getPriority().toString());
        priorityTextArea.setEditable(false);
    }
}
