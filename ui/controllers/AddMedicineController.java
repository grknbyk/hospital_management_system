package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Medicine;
import model.MedicineSupply;
import model.enums.MedicineType;
import model.enums.Status;

public class AddMedicineController {

    @FXML
    private TextField medicineNameTextField;

    @FXML
    private ChoiceBox<MedicineType> typeChoiceBox;

    @FXML
    private Spinner<Integer> amountSpinner;

    public void initializeField() {
        this.typeChoiceBox.getItems().addAll(MedicineType.values());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 200);
        amountSpinner.setValueFactory(valueFactory);
        amountSpinner.getValueFactory().setValue(20);
    }

    public boolean addMedicine() {
        if(typeChoiceBox.getValue() == null || medicineNameTextField.getText().equalsIgnoreCase("") )
            return false;

        try {
            Medicine med = new Medicine(this.medicineNameTextField.getText().trim(), typeChoiceBox.getValue());
            int amount = amountSpinner.getValue();

            if (med.getName().equals("") || med.getType() == null) {
                new Alert(Alert.AlertType.ERROR, "An error occurred while adding medicine").showAndWait();
                return false;
            }

            Datasource.getInstance().addNewMedicine(med, amount);
            MedicineSupply.getInstance().setStock(med, amount);
            return true;
        } catch (Exception e) {
            System.out.println("An error occured while adding medicine");
            new Alert(Alert.AlertType.ERROR, "An error occurred while adding medicine").showAndWait();
            return false;
        }
    }
}
