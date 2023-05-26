package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {
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
    private TableView<Patient> tableView;

    @FXML
    private MenuItem logoutMenuItem;

    private ObservableList<Patient> patients;



    @FXML
    private MenuButton options;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);

//        // Initialize the TableView columns
//        TableColumn<Patient, Integer> idColumn = new TableColumn<>("ID");
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Age");
//        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
//
//        // Add the columns to the TableView
//        tableView.getColumns().addAll(idColumn, nameColumn, ageColumn);
//
//        // Create sample patients
//        patients = FXCollections.observableArrayList(
//                new Patient(1, "John Doe", 30),
//                new Patient(2, "Jane Smith", 25),
//                new Patient(3, "Alice Johnson", 35)
//        );
//
//        // Set the patient data to the TableView
//        tableView.setItems(patients);
    }

    public static class Patient {
        private int id;
        private String name;
        private int age;

        public Patient(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    public void showProfileDialog(){

        Dialog<ButtonType> dialog2 = new Dialog<ButtonType>();
        dialog2.initOwner(DoctorPanel.getScene().getWindow());
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

        Optional<ButtonType> result = dialog2.showAndWait();
        if(result.isPresent() && result.get() == editButton) {
            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.initOwner(DoctorPanel.getScene().getWindow());
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

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }
}
