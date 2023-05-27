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

public class ProfileViewBuilder {
    private BorderPane owner;
    private Staff staff;
    private ProfileController profileController;

    public ProfileViewBuilder() {
        // Default constructor implementation
    }

    public ProfileViewBuilder(String username, BorderPane owner){
        this.owner = owner;
        this.profileController = new ProfileController();
        this.staff = Datasource.getInstance().queryStaffProfile(username);   
        showProfileView();
    }

    private void applyButtonFunction(Staff staff){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Are you sure you want to save the changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Datasource.getInstance().updateStaffProfile(staff);
            showProfileView();
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            showEditProfile();
        }
    }

    private void showEditProfile(){
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(owner.getScene().getWindow());
        dialog.setTitle("Edit Profile");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/profile/EditProfile.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            ProfileController profileController = fxmlLoader.getController();
            profileController.showEdit(staff);
        }catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }


        ButtonType applyButton = new ButtonType("Apply");
        ButtonType cancelButton = new ButtonType("Cancel");

        dialog.getDialogPane().getButtonTypes().addAll(applyButton, cancelButton);

        Optional<ButtonType> result2 = dialog.showAndWait();
        if(result2.isPresent() && result2.get() == applyButton) {
            Staff staff = profileController.returnEditedProfile();
            applyButtonFunction(staff);
        }else if(result2.isPresent() && result2.get() == cancelButton){
            dialog.close();
            showProfileView();
        }
    }

    public void showProfileView(){

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(owner.getScene().getWindow());
        dialog.setTitle("Profile");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/profile/ProfileDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            profileController = fxmlLoader.getController();
            profileController.showFields(staff);
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ButtonType editButton = new ButtonType("Edit");
        ButtonType closeButton = new ButtonType("Close");
        dialog.getDialogPane().getButtonTypes().addAll(editButton, closeButton);

        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog.close();
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == editButton) {
            showEditProfile();
        }else if(result.isPresent() && result.get() == closeButton){
            dialog.close();
        }
    }
}
