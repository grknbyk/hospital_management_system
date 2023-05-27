package ui.controllers;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

import database.Datasource;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.application.Application;

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

    public void showAboutDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Help");

        // Create labels and fields for email, phone, and website
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField("group1_oop@email.com");
        emailField.setEditable(false);
        emailField.setPrefWidth(200);
        emailField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                copyToClipboard(emailField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
            }
        });

        Label phoneLabel = new Label("Phone:");
        Label phoneField = new Label("+1 123-456-7890");

        Hyperlink websiteLink = new Hyperlink("Website: https://ceng.deu.edu.tr");
        websiteLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://ceng.deu.edu.tr"));
            } catch (IOException | URISyntaxException e) {
                // Handle any errors that occur while trying to open the link
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open website.");
            }
        });

        // Create a text area for additional instructions or information
        Label additionalInfoLabel = new Label();
        additionalInfoLabel.setText("This project is designed for OOP lecture in Dokuz Eylul University");

        // Create a button to close the dialog
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialogStage.close());

        // Create a grid pane to hold the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(emailLabel, 0, 0);
        gridPane.add(emailField, 1, 0);
        gridPane.add(phoneLabel, 0, 1);
        gridPane.add(phoneField, 1, 1);
        gridPane.add(websiteLink, 0, 2, 2, 1);
        gridPane.add(additionalInfoLabel, 0, 3, 2, 1);
        gridPane.add(closeButton, 0, 4, 2, 1);

        // Set the scene for the dialog and show it
        Scene dialogScene = new Scene(gridPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
