package ui.controllers;

import database.Datasource;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Staff;

public class ManagerController {

    @FXML
    private BorderPane managerPanel;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    public void listStaffs() {
        Task<ObservableList<Staff>> task = new GetAllStaffsTask();
        //artistTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    public void initialize() {
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

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(managerPanel.getScene().getWindow());
        dialog.setTitle("Profile");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/ProfileDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result = dialog.showAndWait();
    }

	
}
class GetAllStaffsTask extends Task {
    @Override
    public ObservableList<Staff> call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryStaff());
    }
}
