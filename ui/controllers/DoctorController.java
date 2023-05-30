package ui.controllers;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import database.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Patient;
import model.Receipt;
import model.enums.EmergencyState;
import model.enums.Priority;

public class DoctorController {

    private String username;

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
    private TableColumn<Patient, EmergencyState> emergencyColumn;

    @FXML
    private TableColumn<Patient, Priority> priorityColumn;

    @FXML
    private TableColumn<Patient, LocalDateTime> appointmentColumn;



    @FXML
    private MenuButton options;

    public void initialize() {

        

        priorityColumn.setCellFactory(val -> new TableCell<Patient, Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    if (item == Priority.HIGH) {
                        setStyle("-fx-background-color: rgba(255, 0, 0, 0.4);");
                    } else if (item == Priority.MEDIUM) {
                        setStyle("-fx-background-color: rgba(255, 255, 0, 0.4);");
                    } else {
                        setStyle("-fx-background-color: rgba(0, 255, 0, 0.4);");
                    }
                }
            }
        });

        emergencyColumn.setCellFactory(val -> new TableCell<Patient, EmergencyState>() {
            @Override
            protected void updateItem(EmergencyState item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    if (item == EmergencyState.NON_URGENT) {
                        setStyle("-fx-background-color: rgba(0, 255, 0, 0.4);");
                    } else if (item == EmergencyState.STABLE) {
                        setStyle("-fx-background-color: rgba(0, 0, 255, 0.4);");
                    } else if (item == EmergencyState.URGENT) {
                        setStyle("-fx-background-color: rgba(255, 0, 255, 0.4);");
                    } else if (item == EmergencyState.CRITICAL) {
                        setStyle("-fx-background-color: rgba(255, 255, 0, 0.4);");
                    }else {
                        setStyle("-fx-background-color: rgba(255, 0, 0, 0.4);");
                    }
                }
            }
        });

        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, DoctorPanel).showProfileView();
    }

    public void loadPatients() {
        // fill the table
        int staffId = Datasource.getInstance().queryStaffId(username);
        patients = FXCollections.observableArrayList(Datasource.getInstance().queryPatients(staffId));
        patientTableView.setItems(patients);
    }


    public void showHelpDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Help");

        // Create a text area for additional instructions or information
        Label additionalInfoLabel = new Label();
        additionalInfoLabel.setText(
                "Contact us via our contact addresses for error reporting. \nCheck out our tutorial content on our website.");

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
        additionalInfoLabel.setText(
                "Who are we?\nWe are computer engineering students at Dokuz Eylül University. \nWe developed this project for our school's OOP class. You can contact us via the following e-mail addresses.");

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

    public void showPrescribe() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the patient to write prescribe");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(DoctorPanel.getScene().getWindow());
        dialog.setTitle("Prescribe");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/receipt/PatientPrescribe.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            PatientPrescribeController patientPrescribeController = fxmlLoader.getController();
            patientPrescribeController.updateFields(selectedPatient);
            int patientId = selectedPatient.getId();
            int doctorId = Datasource.getInstance().queryStaffId(username);
            LocalDate currentDate = LocalDate.now();
            Date sqlDate = Date.valueOf(currentDate);
            LocalDate localDate = sqlDate.toLocalDate(); // Convert java.sql.Date to java.time.LocalDate
            LocalDate newDate = localDate.plusWeeks(4); // Add 4 weeks to the date
            Date newSqlDate = Date.valueOf(newDate);
            Receipt receipt = new Receipt(0, patientId, doctorId, sqlDate, newSqlDate);
            patientPrescribeController.setReceipt(receipt);
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = dialog.showAndWait();
    }

    public void showPatientData() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the patient you want to retrieve data from.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(DoctorPanel.getScene().getWindow());
        dialog.setTitle("Patient Data");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/patient/PatientData.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            PatientDataController patientDataController = fxmlLoader.getController();
            patientDataController.updateFields(selectedPatient);
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result = dialog.showAndWait();
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/panels/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
        new ProfileViewBuilder(username, DoctorPanel);
    }
}
