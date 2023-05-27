package ui.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Medicine;
import model.MedicineSupply;
import utils.Pair;

public class PharmacistController {
    String username;

    @FXML
    private BorderPane pharmacistPanel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private TableView<Pair<Medicine, Integer>> medicineTableView;
    private ObservableList<Pair<Medicine, Integer>> medicine;

    @FXML
    private MenuButton options;


    public void initialize() {

        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);
    }

    public void loadMedicine() {
        //fill the table
        int staffId = Datasource.getInstance().queryStaffId(username);
        Datasource.getInstance().updateMedicineSupply(MedicineSupply.getInstance());
        medicine = FXCollections.observableList(Arrays.asList(MedicineSupply.getInstance().toList().toArray()).stream().map(obj -> (Pair<Medicine, Integer>) obj).collect(Collectors.toList()));
        medicineTableView.setItems(medicine);
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, pharmacistPanel);
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }
}
