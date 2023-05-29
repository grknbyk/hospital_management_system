package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Medicine;
import model.enums.MedicineType;
import model.enums.Status;

public class AddMedicineController {

    @FXML
    private TextField medicineNameTextField;

    @FXML
    private ChoiceBox<MedicineType> typeChoiceBox;

    @FXML
    private TextField amountTextField;

    public void initializeField() {
        this.typeChoiceBox.getItems().addAll(MedicineType.values());
    }

    public boolean addMedicine() {
        try {
            Medicine med = new Medicine(this.medicineNameTextField.getText().trim(), typeChoiceBox.getValue());
            int amount = Integer.parseInt(amountTextField.getText());

            if (med.getName().equals("") || med.getType() == null) {
                new Alert(Alert.AlertType.ERROR, "An error occurred while adding medicine").showAndWait();
                return false;
            }

            Datasource.getInstance().addNewMedicine(med, amount);
            return true;
        } catch (Exception e) {
            System.out.println("An error occured while adding medicine");
            new Alert(Alert.AlertType.ERROR, "An error occurred while adding medicine").showAndWait();
            return false;
        }
    }
}
