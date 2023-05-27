package ui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;

public class ManagerController {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private TableView<Doctor> tableView;


    @FXML
    private BorderPane managerPanel;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuButton options;

    @FXML
    private TableView<Doctor> doctorTableView;
    private ObservableList<Doctor> doctors;

    @FXML
    private TableView<Nurse> nurseTableView;
    private ObservableList<Nurse> nurses;

    @FXML
    private TableView<Pharmacist> pharmacistTableView;
    private ObservableList<Pharmacist> pharmacists;

    @FXML
    private TableView<Receptionist> receptionistTableView;
    private ObservableList<Receptionist> receptionists;

    public void initialize() {
        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);

        //fill the table
        doctors = FXCollections.observableArrayList(Datasource.getInstance().queryDoctors());
        doctorTableView.setItems(doctors);

        nurses = FXCollections.observableArrayList(Datasource.getInstance().queryNurse());
        nurseTableView.setItems(nurses);

        pharmacists = FXCollections.observableArrayList(Datasource.getInstance().queryPharmacists());
        pharmacistTableView.setItems(pharmacists);

        receptionists = FXCollections.observableArrayList(Datasource.getInstance().queryRecepsionist());
        receptionistTableView.setItems(receptionists);

    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }

    public void showProfileDialog(){
        new ProfileViewBuilder(username,managerPanel);
    }
	
}
