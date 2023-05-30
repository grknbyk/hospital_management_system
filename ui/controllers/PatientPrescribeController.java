package ui.controllers;

import java.util.ArrayList;
import java.util.Optional;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Medicine;
import model.Patient;
import model.Receipt;
import model.enums.MedicineType;

public class PatientPrescribeController {
    private Receipt receipt;

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @FXML
    private TextArea complaintTextArea;
    @FXML
    private TextArea nameTextArea;

    @FXML
    private TableView<Receipt.ReceiptItem> tableView;

    @FXML
    private TableColumn<Receipt.ReceiptItem, String> medicineColumn;

    @FXML
    private TableColumn<Receipt.ReceiptItem, MedicineType> typeColumn;

    @FXML
    private TableColumn<Receipt.ReceiptItem, Integer> amountColumn;

    @FXML
    private ComboBox<MedicineType> medTypeComboBox;

    @FXML
    private ComboBox<String> medNameComboBox;

    @FXML
    private Spinner<Integer> amountSpinner;

    public void updateFields(Patient patient) {
        nameTextArea.setText(patient.getName() + " " + patient.getSurname());
        complaintTextArea.setText(patient.getComplaint());

        amountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200));
    }

    @FXML
    public void initialize() {
        ArrayList<Medicine> medicines = Datasource.getInstance().queryMedicine();
        medTypeComboBox.getItems().addAll(MedicineType.values());
        medTypeComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            medNameComboBox.getItems().clear();
            if (newValue != null) {
                for (Medicine medicine : medicines) {
                    if (medicine.getType() == newValue) {
                        medNameComboBox.getItems().add(medicine.getName());
                    }
                }
                medNameComboBox.getSelectionModel().select(0);
            }

        });
    }

    @FXML
    private void addButtonClicked() {
        String name = medNameComboBox.getSelectionModel().getSelectedItem();
        MedicineType type = medTypeComboBox.getSelectionModel().getSelectedItem();
        if (name == null || type == null) {
            return;
        }

        Medicine medicine = new Medicine();
        int id = Datasource.getInstance().queryMedicineIdByName(name);
        medicine.setId(id);
        medicine.setName(name);
        medicine.setType(type);
        int amount = amountSpinner.getValue();

        // Check if the medicine already exists in the table
        for (Receipt.ReceiptItem item : tableView.getItems()) {
            if (item.getName().equals(medicine.getName())) {
                // Medicine already exists, increase the amount
                item.setAmount(amount + item.getAmount());
                tableView.refresh(); // Refresh the table to update the view
                return; // Exit the method
            }
        }

        // Medicine does not exist, add it as a new row
        Receipt.ReceiptItem newItem = new Receipt.ReceiptItem(medicine, amount);
        tableView.getItems().add(newItem);
    }

    @FXML
    private void removeButtonClicked() {
        Receipt.ReceiptItem item = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(item);
    }

    @FXML
    private void prescribeButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to save the changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tableView.getItems().forEach(item -> {
                Medicine medicine = new Medicine();
                medicine.setId(item.getId());
                medicine.setName(item.getName());
                medicine.setType(item.getType());
                receipt.add(medicine,item.getAmount());
            });
            Datasource.getInstance().addNewReceipt(receipt);
            alert.close();
            closeWindow();
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            alert.close();
        }

    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

}
