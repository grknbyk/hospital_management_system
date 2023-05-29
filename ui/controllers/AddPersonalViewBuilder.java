package ui.controllers;

import java.io.IOException;
import java.util.Optional;

import database.Datasource;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import model.Staff;

public class AddPersonalViewBuilder {
    private BorderPane owner;
    private AddPersonnalController controller;
    private String username;

    public AddPersonalViewBuilder() {
        // Default constructor implementation
    }

    public AddPersonalViewBuilder(String username, BorderPane owner){
        this.owner = owner;
        this.username = username;
    }

    private void applyButtonFunction(Staff staff){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to save the changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Datasource.getInstance().addNewStaff(staff);
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            showAddUserView();
        }
    }

    

    public void showAddUserView(){

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(owner.getScene().getWindow());
        dialog.setTitle("Staff");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/user/AddPersonal.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ButtonType addButton = new ButtonType("Add");
        ButtonType closeButton = new ButtonType("Close");
        dialog.getDialogPane().getButtonTypes().addAll(addButton, closeButton);

        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog.close();
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == addButton) {
            controller = fxmlLoader.getController();
            Staff staff = controller.returnStaff();
            applyButtonFunction(staff);
        }else if(result.isPresent() && result.get() == closeButton){
            dialog.close();
        }
    }
}
