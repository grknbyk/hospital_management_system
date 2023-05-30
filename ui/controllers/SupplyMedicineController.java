package ui.controllers;

import database.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import model.MedicineSupply;

public class SupplyMedicineController {

    @FXML
    private Spinner<Integer> amountSpinner;

    @FXML
    private Label inStockLabel;

    @FXML
    private Label nameLabel;

    public void updateFields(MedicineSupply.SupplyItem selectedItem) {
        nameLabel.setText(selectedItem.getName());
        inStockLabel.setText(String.valueOf(selectedItem.getStock()));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 200);
        amountSpinner.setValueFactory(valueFactory);
        amountSpinner.getValueFactory().setValue(20);
    }

    public boolean increaseAmount(MedicineSupply supply, MedicineSupply.SupplyItem selectedItem) {
        supply.supplyStock(selectedItem.getMedicine(), amountSpinner.getValue());
        Datasource.getInstance().saveMedicineSupply(supply);
        return true;
    }
}
