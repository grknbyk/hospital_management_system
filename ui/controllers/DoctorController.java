package ui.controllers;

import model.*;
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

import java.io.IOException;
import java.util.Optional;

public class DoctorController{

    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private BorderPane DoctorPanel;


    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private TableView<Patient> patientTableView;
    private ObservableList<Patient> patients;

    @FXML
    private MenuButton options;


    public void initialize() {

        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);
    }

    public void loadPatients(){
        //fill the table
        int staffId = Datasource.getInstance().queryStaffId(username);
        patients = FXCollections.observableArrayList(Datasource.getInstance().queryPatients(staffId));
        patientTableView.setItems(patients);
    }

    public void showProfileDialog(){
        new ProfileViewBuilder(username, DoctorPanel);
    }
}
