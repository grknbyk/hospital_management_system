package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import model.Medicine;
import model.MedicineSupply;
import model.Receipt;

import java.util.ArrayList;
import java.util.TreeMap;

public class DispenseMedicineController {
    private DispenseElement selectedItem;

    @FXML
    private BorderPane dispenseMedicineBorderPane;

    private TreeMap<Medicine, MedicineSupply.SupplyItem> medicinesTreeMap;

    @FXML
    private TableView<DispenseElement> dispenseTableView;
    private ObservableList<DispenseElement> dispenseElementObservableList;

    public void updateFields(Receipt selectedReceipt, TreeMap<Medicine, MedicineSupply.SupplyItem> medicinesTreeMap) {
        this.medicinesTreeMap = medicinesTreeMap;
        ArrayList<Receipt.ReceiptItem> receiptItems = new ArrayList<>(selectedReceipt.getContent().values());

        ArrayList<DispenseElement> dispenseElements = new ArrayList<>();
        for(int i = 0 ; i< receiptItems.size() ; i++){
            int inStock = medicinesTreeMap.get(receiptItems.get(i).getMed()).getStock();
            DispenseElement dispenseElement = new DispenseElement(receiptItems.get(i).getMed(), receiptItems.get(i).getAmount(), inStock);
            dispenseElements.add(dispenseElement);
        }

        dispenseElementObservableList = FXCollections.observableArrayList(dispenseElements);
        dispenseTableView.setItems(dispenseElementObservableList);
    }

    public boolean dispenseReceipt(Receipt selectedReceipt) {

        this.selectedItem = dispenseTableView.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            return false;
        }
        if(selectedItem.demand <= selectedItem.inStock){
            MedicineSupply.getInstance().consumeStock(selectedItem.medicine,selectedItem.demand);
            return true;
        }
        return false;
    }

    public DispenseElement getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(DispenseElement selectedItem) {
        this.selectedItem = selectedItem;
    }

    public class DispenseElement {
        private Medicine medicine;
        private int demand;
        private int inStock;

        public DispenseElement() {
            this.medicine = new Medicine();
            this.demand = 0;
            this.inStock = 0;
        }

        public DispenseElement(Medicine medicineName, int demand, int inStock) {
            this.medicine = medicineName;
            this.demand = demand;
            this.inStock = inStock;
        }

        public Medicine getMedicine() {
            return medicine;
        }

        public void setMedicine(Medicine medicine) {
            this.medicine = medicine;
        }

        public int getDemand() {
            return demand;
        }

        public void setDemand(int demand) {
            this.demand = demand;
        }

        public int getInStock() {
            return inStock;
        }

        public void setInStock(int inStock) {
            this.inStock = inStock;
        }
    }

}
