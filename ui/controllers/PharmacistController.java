package ui.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.MedicineSupply;
import model.Receipt;


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
    private TableView<MedicineSupply.SupplyItem> medicineTableView;
    private ObservableList<MedicineSupply.SupplyItem> medicine;
    @FXML
    private TableView<Receipt> receiptsTableView;
    private ObservableList<Receipt> receipts;

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
        Datasource.getInstance().updateMedicineSupply(MedicineSupply.getInstance());
        medicine = FXCollections.observableList(Arrays.stream(MedicineSupply.getInstance().toList().toArray()).map(obj -> (MedicineSupply.SupplyItem) obj).collect(Collectors.toList()));
        medicineTableView.setItems(medicine);
    }

    public void loadReceipts() {
        ArrayList<Receipt> arr = Datasource.getInstance().queryReceipts();
        receipts = FXCollections.observableList(arr);
        receiptsTableView.setItems(receipts);
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, pharmacistPanel).showProfileView();
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }

    public void dispenseMedicine() {

    }

    public void showReceiptDetails() {
        Receipt selectedReceipt = receiptsTableView.getSelectionModel().getSelectedItem();
        if(selectedReceipt == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the patient you want to retrieve data from.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pharmacistPanel.getScene().getWindow());
        dialog.setTitle("Receipt Details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/ReceiptDetails.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            ReceiptDetailsController receiptDetailsController = fxmlLoader.getController();
            receiptDetailsController.updateFields(selectedReceipt);
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    public void showHelpDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Help");

        // Create a text area for additional instructions or information
        Label additionalInfoLabel = new Label();
        additionalInfoLabel.setText("Contact us via our contact addresses for error reporting. \nCheck out our tutorial content on our website.");

        // Create labels and fields for email, phone, and website
        Label emailLabel = new Label("Email:");
        Hyperlink emailField = new Hyperlink("group1_oop@email.com");
        emailField.setPrefWidth(200);
        emailField.setOnMouseClicked(event -> {
            copyToClipboard(emailField.getText());
            showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
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

        // Create a button to close the dialog
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialogStage.close());

        // Create a grid pane to hold the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(additionalInfoLabel, 0, 0, 2, 1);
        gridPane.add(emailLabel, 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(phoneField, 1, 2);
        gridPane.add(websiteLink, 0, 3, 2, 1);
        gridPane.add(closeButton, 5, 4, 2, 1);

        // Set the scene for the dialog and show it
        Scene dialogScene = new Scene(gridPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    public void showAboutDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("About");

        // Create a text area for additional instructions or information
        Label additionalInfoLabel = new Label();
        additionalInfoLabel.setText("Who are we?\nWe are computer engineering students at Dokuz Eylül University. \nWe developed this project for our school's OOP class. You can contact us via the following e-mail addresses.");

        // Create labels and fields for email, phone, and website
        Label emailLabel = new Label("Abdulkadir Öksüz:");
        Hyperlink emailField = new Hyperlink("abdulkadir.oksuz@outlook.com");
        emailField.setPrefWidth(200);
        emailField.setOnMouseClicked(event -> {
            copyToClipboard(emailField.getText());
            showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
        });

        Label emailLabel2 = new Label("Can Çelenay:");
        Hyperlink emailField2 = new Hyperlink("brogolem35@protonmail.com");
        emailField2.setPrefWidth(200);
        emailField2.setOnMouseClicked(event -> {
            copyToClipboard(emailField2.getText());
            showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
        });

        Label emailLabel3 = new Label("Gürkan Bıyık:");
        Hyperlink emailField3 = new Hyperlink("email");
        emailField3.setPrefWidth(200);
        emailField3.setOnMouseClicked(event -> {
            copyToClipboard(emailField3.getText());
            showAlert(Alert.AlertType.INFORMATION, "Copied", "Email address copied to clipboard.");
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

        // Create a button to close the dialog
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialogStage.close());

        // Create a grid pane to hold the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(additionalInfoLabel, 0, 0, 2, 1);
        gridPane.add(emailLabel, 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(emailLabel2, 0, 2);
        gridPane.add(emailField2, 1, 2);
        gridPane.add(emailLabel3, 0, 3);
        gridPane.add(emailField3, 1, 3);
        gridPane.add(phoneLabel, 0, 4);
        gridPane.add(phoneField, 1, 4);
        gridPane.add(websiteLink, 0, 5, 2, 1);
        gridPane.add(closeButton, 5, 6, 2, 1);

        // Set the scene for the dialog and show it
        Scene dialogScene = new Scene(gridPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    public void addMedicine() {
        AddMedicineController addMedicineController;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pharmacistPanel.getScene().getWindow());
        dialog.setTitle("Add Medicine");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/AddMedicine.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            addMedicineController = fxmlLoader.getController();
            addMedicineController.initializeField();
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog.close();
        });

        ButtonType applyButton = new ButtonType("Apply");
        ButtonType cancelButton = new ButtonType("Cancel");

        dialog.getDialogPane().getButtonTypes().addAll(applyButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == applyButton) {
            applyButtonFunction(addMedicineController);
        }else if(result.isPresent() && result.get() == cancelButton){
            dialog.close();
        }
    }


    private void errorDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occured");
        alert.setContentText("Enter medicine name and type");
        alert.showAndWait();
    }

    private void applyButtonFunction(AddMedicineController addMedicineController){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want add?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!addMedicineController.addMedicine()){
                errorDialog();
                addMedicine();
            }
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            addMedicine();
        }

        loadMedicine();
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

}
