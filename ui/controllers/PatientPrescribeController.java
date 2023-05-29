package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Medicine;
import model.Patient;
import model.Receipt;

import java.util.ArrayList;

public class PatientPrescribeController {

    @FXML
    private TextArea complaintTextArea;
    @FXML
    private TextArea nameTextArea;

    @FXML
    private TableView<Receipt.ReceiptItem> tableView;

    @FXML
    private TableColumn<Receipt.ReceiptItem, String> medicineColumn;

    @FXML
    private TableColumn<Receipt.ReceiptItem, Integer> amountColumn;
    ObservableList<Receipt.ReceiptItem> items = FXCollections.observableArrayList();

    @FXML
    private TextField medicineNameField;

    @FXML
    private Spinner<Integer> amountSpinner;

    public void updateFields(Patient patient) {
        nameTextArea.setText(patient.getName() + " " + patient.getSurname());
        complaintTextArea.setText(patient.getComplaint());

        amountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200));

        tableView.setItems(items);
    }


    @FXML
    private void addButtonClicked() {
        String medicineName = medicineNameField.getText();
        if(medicineName.equals(""))
            return;

        int amount = amountSpinner.getValue();
        Medicine med = new Medicine();
        med.setName(medicineName);

        Receipt.ReceiptItem newItem = new Receipt.ReceiptItem(med, amount);
        items.add(newItem);

        // Clear the input fields
        medicineNameField.clear();
        amountSpinner.getValueFactory().setValue(0);
    }

}
