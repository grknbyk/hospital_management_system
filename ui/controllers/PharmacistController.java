package ui.controllers;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

import database.Datasource;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.Medicine;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MedicineSupply;
import model.Patient;
import model.Person;
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
    private TabPane pharmacistTabPane;

    @FXML
    private Tab stockTab;

    @FXML
    private Tab receiptsTab;

    @FXML
    private TableView<MedicineSupply.SupplyItem> medicineTableView;
    private ObservableList<MedicineSupply.SupplyItem> medicine;

    @FXML
    private TableView<Receipt> receiptsTableView;
    private ObservableList<Receipt> receipts;

    @FXML
    private TableColumn<Receipt, String> patientNameColumn;

    @FXML
    private TableColumn<Receipt, String> patientSurnameColumn;

    @FXML
    private TableColumn<Receipt, Date> expireDateColumn;

    @FXML
    private TableColumn<MedicineSupply.SupplyItem, Integer> inStockColumn;

    private ObservableList<Person> patients;

    @FXML
    private MenuButton options;

    private TreeMap<Medicine, MedicineSupply.SupplyItem> medicinesTreeMap;

    public void initialize() {

        inStockColumn.setCellFactory(val -> new TableCell<MedicineSupply.SupplyItem, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item >= 150) {
                        setStyle("-fx-background-color: rgba(0, 255, 0, 0.4);");
                    } else if (item >= 100) {
                        setStyle("-fx-background-color: rgba(180, 255, 0, 0.4);");
                    } 
                    else if (item >= 50) {
                        setStyle("-fx-background-color: rgba(255, 200, 0, 0.4);");
                    } 
                    else if (item >= 30) {
                        setStyle("-fx-background-color: rgba(255, 100, 0, 0.4);");
                    } 
                    else {
                        setStyle("-fx-background-color: rgba(255, 0, 0, 0.4);");
                    }
                }
            }
        });
        expireDateColumn.setCellFactory(val -> new TableCell<Receipt, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
        

                    if (LocalDate.now().isAfter(item.toLocalDate())) {
                        setStyle("-fx-background-color: rgba(255, 0, 0, 0.4);");
                    } else if(LocalDate.now().equals(item.toLocalDate())) {
                        setStyle("-fx-background-color: rgba(255, 255, 0, 0.4);");
                    }else{
                        setStyle("-fx-background-color: rgba(0, 255, 0, 0.4);");
                    }
                }
            }
        });
        

        patientNameColumn.setCellValueFactory(arg0 -> {
            int id = arg0.getValue().getPatientId();
            for (Person p : patients) {
                if (p.getId() == id) {
                    return new SimpleStringProperty(p.getName());
                }
            }
            return new SimpleStringProperty("");
        });

        patientSurnameColumn.setCellValueFactory(arg0 -> {
            int id = arg0.getValue().getPatientId();
            for (Person p : patients) {
                if (p.getId() == id) {
                    return new SimpleStringProperty(p.getSurname());
                }
            }
            return new SimpleStringProperty("");
        });

        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        options.setGraphic(imageView);
    }

    public void loadMedicine() {
        // fill the table
        Datasource.getInstance().updateMedicineSupply(MedicineSupply.getInstance());
        medicinesTreeMap = MedicineSupply.getInstance().getInventory();
        medicine = FXCollections.observableList(Arrays.stream(MedicineSupply.getInstance().toList().toArray())
                .map(obj -> (MedicineSupply.SupplyItem) obj).collect(Collectors.toList()));
        medicineTableView.setItems(medicine);
    }

    public void loadReceipts() {

        ArrayList<Receipt> arr = Datasource.getInstance().queryReceipts();
        ArrayList<Person> patients = new ArrayList<>();
        for (Receipt r : arr) {
            int patientId = r.getPatientId();
            Person p = Datasource.getInstance().queryPersonbyId(patientId);
            patients.add(p);
        }
        this.patients = FXCollections.observableList(patients);
        receipts = FXCollections.observableList(arr);
        receiptsTableView.setItems(receipts);
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, pharmacistPanel).showProfileView();
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../scene/panels/LoginScene.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) logoutMenuItem.getParentPopup().getOwnerNode().getScene().getWindow();
        window.setTitle("Hospital Management System");
        window.setScene(scene);
        window.show();
    }

    public void dispenseMedicine() {
        Tab selectedTab = pharmacistTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            if (selectedTab.equals(receiptsTab)) {

                Receipt selectedReceipt = receiptsTableView.getSelectionModel().getSelectedItem();

                if (selectedReceipt == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Receipt Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Select a Receipt");
                    alert.showAndWait();
                    return;
                }
                if (LocalDate.now().isAfter(selectedReceipt.getExpireDate().toLocalDate())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Expired Receipt");
                    alert.setHeaderText(null);
                    alert.setContentText("Please renew the receipt");
                    alert.showAndWait();
                    return;
                }
                DispenseMedicineController dispenseMedicineController;

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pharmacistPanel.getScene().getWindow());
                dialog.setTitle("Dispense Medicine");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../scene/medicine/DispenseMedicine.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                    dispenseMedicineController = fxmlLoader.getController();
                    dispenseMedicineController.updateFields(selectedReceipt, medicinesTreeMap);
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
                if (result.isPresent() && result.get() == applyButton) {
                    applyButtonFunctionDispense(selectedReceipt, dispenseMedicineController);
                } else if (result.isPresent() && result.get() == cancelButton) {
                    dialog.close();
                }

            }
        }
    }

    private void applyButtonFunctionDispense(Receipt selectedReceipt,
            DispenseMedicineController dispenseMedicineController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to dispense?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dispenseMedicineController.dispenseReceipt(selectedReceipt).forEach(medicine -> {
                selectedReceipt.remove(medicine);
                selectedReceipt.setGiven(true);
                Datasource.getInstance().updateReceipIsGiven(selectedReceipt.getId(), true);
            });
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            dispenseMedicine();
        }
        Datasource.getInstance().saveMedicineSupply(MedicineSupply.getInstance());
        loadMedicine();
    }

    public void showReceiptDetails() {
        Receipt selectedReceipt = receiptsTableView.getSelectionModel().getSelectedItem();
        if (selectedReceipt == null) {
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
        fxmlLoader.setLocation(getClass().getResource("../scene/receipt/ReceiptDetails.fxml"));
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

    public void addMedicine() {
        AddMedicineController addMedicineController;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pharmacistPanel.getScene().getWindow());
        dialog.setTitle("Add Medicine");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/medicine/AddMedicine.fxml"));
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
        if (result.isPresent() && result.get() == applyButton) {
            applyButtonFunctionAdd(addMedicineController);
        } else if (result.isPresent() && result.get() == cancelButton) {
            dialog.close();
        }
    }

    public void supplyMedicine() {
        Tab selectedTab = pharmacistTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            if (selectedTab.equals(stockTab)) {

                MedicineSupply.SupplyItem selectedItem = medicineTableView.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Item Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Select an item");
                    alert.showAndWait();
                    return;
                }
                SupplyMedicineController supplyMedicineController;

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pharmacistPanel.getScene().getWindow());
                dialog.setTitle("Supply Medicine");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../scene/medicine/SupplyMedicine.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                    supplyMedicineController = fxmlLoader.getController();
                    supplyMedicineController.updateFields(selectedItem);
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
                if (result.isPresent() && result.get() == applyButton) {
                    applyButtonFunctionSupply(selectedItem, supplyMedicineController);
                } else if (result.isPresent() && result.get() == cancelButton) {
                    dialog.close();
                }

            }
        }
    }

    private void errorDialogAdd() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occured");
        alert.setContentText("Enter an amount");
        alert.showAndWait();
    }

    private void errorDialogSupply() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occured while giving an order.");
        alert.setContentText("You can find our email in help button. \nPlease report us");
        alert.showAndWait();
    }

    private void applyButtonFunctionAdd(AddMedicineController addMedicineController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want add selected amount?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!addMedicineController.addMedicine()) {
                errorDialogAdd();
                addMedicine();
            }
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            addMedicine();
        }
        loadMedicine();
    }

    private void applyButtonFunctionSupply(MedicineSupply.SupplyItem selectedItem,
            SupplyMedicineController supplyMedicineController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want add selected amount?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!supplyMedicineController.increaseAmount(MedicineSupply.getInstance(), selectedItem)) {
                errorDialogSupply();
                Datasource.getInstance().saveMedicineSupply(MedicineSupply.getInstance());
            }
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            supplyMedicine();
        }

        Datasource.getInstance().saveMedicineSupply(MedicineSupply.getInstance());
        loadMedicine();
    }

    public void reduceMedicine() {
        Tab selectedTab = pharmacistTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            if (selectedTab.equals(stockTab)) {

                MedicineSupply.SupplyItem selectedItem = medicineTableView.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Item Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Select an item");
                    alert.showAndWait();
                    return;
                }
                ReduceMedicineController reduceMedicineController;

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(pharmacistPanel.getScene().getWindow());
                dialog.setTitle("Reduce Medicine");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../scene/medicine/ReduceMedicine.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                    reduceMedicineController = fxmlLoader.getController();
                    reduceMedicineController.updateFields(selectedItem);
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
                if (result.isPresent() && result.get() == applyButton) {
                    applyButtonFunctionReduce(selectedItem, reduceMedicineController);
                } else if (result.isPresent() && result.get() == cancelButton) {
                    dialog.close();
                }

            }
        }
    }

    private void errorDialogReduce() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred during reduction.");
        alert.setContentText("You can find our email in help button. \nPlease report us");
        alert.showAndWait();
    }

    private void applyButtonFunctionReduce(MedicineSupply.SupplyItem selectedItem,
            ReduceMedicineController reduceMedicineController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want reduce selected amount?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!reduceMedicineController.decreaseAmount(MedicineSupply.getInstance(), selectedItem)) {
                errorDialogReduce();
                reduceMedicine();
            }
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            reduceMedicine();
        }

        Datasource.getInstance().saveMedicineSupply(MedicineSupply.getInstance());
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
