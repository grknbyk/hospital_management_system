package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
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
            Datasource.getInstance().addNewMedicine(new Medicine(this.medicineNameTextField.getText(),typeChoiceBox.getValue()),Integer.parseInt(amountTextField.getText()));
            return true;
        }catch (Exception e) {
            System.out.println("An error occured while adding medicine");
            return false;
        }
    }
}
