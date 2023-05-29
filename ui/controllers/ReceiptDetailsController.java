package ui.controllers;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import model.MedicineSupply;
import model.Patient;
import model.Receipt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptDetailsController {

    @FXML
    private TableView<Receipt.ReceiptItem> tableView;

    public void updateFields(Receipt receipt) {
        ObservableList<Receipt.ReceiptItem> medicine = FXCollections.observableList(new ArrayList<>((List<Receipt.ReceiptItem>) (Object)Arrays.asList(receipt.toList().toArray())));
        tableView.setItems(medicine);
    }
}
