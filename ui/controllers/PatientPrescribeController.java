package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.Patient;

public class PatientPrescribeController {

    @FXML
    private TextArea complaintTextArea;



    public void updateFields(Patient selectedPatient) {
        complaintTextArea.setText(selectedPatient.getComplaint());
        complaintTextArea.setEditable(false);
    }
}
