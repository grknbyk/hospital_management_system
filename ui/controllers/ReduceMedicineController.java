package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.MedicineSupply;

public class ReduceMedicineController {
    @FXML
    private Spinner<Integer> amountSpinner;

    @FXML
    private Label inStockLabel;

    @FXML
    private Label nameLabel;

    public void updateFields(MedicineSupply.SupplyItem selectedItem) {
        nameLabel.setText(selectedItem.getName());
        inStockLabel.setText(String.valueOf(selectedItem.getStock()));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200);
        amountSpinner.setValueFactory(valueFactory);
        amountSpinner.getValueFactory().setValue(10);
    }

    public boolean decreaseAmount(MedicineSupply supply, MedicineSupply.SupplyItem selectedItem) {

        //işlem başarılı olursa true returnlicek
        //olmazsa false returnlicek
        return true;
    }
}
