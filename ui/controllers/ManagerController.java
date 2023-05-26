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
    //Doctor>>>>>>>>>>>>>>>>>>>>>>>>>
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

        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(managerPanel.getScene().getWindow());
        dialog2.setTitle("Profile");
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        fxmlLoader2.setLocation(getClass().getResource("../scene/profile/ProfileDialog.fxml"));
        try {
            dialog2.getDialogPane().setContent(fxmlLoader2.load());
            ProfileController profileController = fxmlLoader2.getController();
            profileController.updateFields(username);
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ButtonType editButton = new ButtonType("Edit");
        ButtonType closeButton = new ButtonType("Close");
        dialog2.getDialogPane().getButtonTypes().addAll(editButton, closeButton);

        dialog2.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog2.close();
        });

        Optional<ButtonType> result = dialog2.showAndWait();
        if(result.isPresent() && result.get() == editButton) {
            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.initOwner(managerPanel.getScene().getWindow());
            dialog.setTitle("Edit Profile");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../scene/profile/EditProfile.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                ProfileController profileController = fxmlLoader.getController();
                profileController.updateEdit(username);
            }catch (IOException e){
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result2 = dialog.showAndWait();
            if(result2.isPresent() && result2.get() == ButtonType.OK) {

                ProfileController profileController = fxmlLoader.getController();
                profileController.editFields(username);
            }

        }
    }

	
}
