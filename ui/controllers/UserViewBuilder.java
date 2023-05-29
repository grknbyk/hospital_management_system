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

public class UserViewBuilder {
    private BorderPane owner;
    private UserController userController;
    private String username;

    public UserViewBuilder() {
        // Default constructor implementation
    }

    public UserViewBuilder(String username, BorderPane owner){
        this.owner = owner;
        this.username = username;
    }

    // private void applyButtonFunction(Staff staff){
    //     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    //     alert.setTitle("Confirmation");
    //     alert.setHeaderText("Confirmation Dialog");
    //     alert.setContentText("Are you sure you want to save the changes?");

    //     Optional<ButtonType> result = alert.showAndWait();
    //     if (result.isPresent() && result.get() == ButtonType.OK) {
    //         Datasource.getInstance().updateStaffProfile(staff);
    //         showProfileView();
    //     } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
    //         showEditProfile();
    //     }
    // }

    // public void showEditProfile(){
    //     Dialog<ButtonType> dialog = new Dialog<ButtonType>();
    //     dialog.initOwner(owner.getScene().getWindow());
    //     dialog.setTitle("Edit Profile");
    //     FXMLLoader fxmlLoader = new FXMLLoader();
    //     fxmlLoader.setLocation(getClass().getResource("../scene/profile/EditProfile.fxml"));

    //     try {
    //         dialog.getDialogPane().setContent(fxmlLoader.load());
    //         profileController = fxmlLoader.getController();
    //         profileController.showEdit(username);
    //     }catch (IOException e){
    //         System.out.println("Couldn't load the dialog");
    //         e.printStackTrace();
    //         return;
    //     }


    //     ButtonType applyButton = new ButtonType("Apply");
    //     ButtonType cancelButton = new ButtonType("Cancel");

    //     dialog.getDialogPane().getButtonTypes().addAll(applyButton, cancelButton);

    //     Optional<ButtonType> result2 = dialog.showAndWait();
    //     if(result2.isPresent() && result2.get() == applyButton) {
    //         applyButtonFunction(profileController.returnEditedProfile());
    //     }else if(result2.isPresent() && result2.get() == cancelButton){
    //         dialog.close();
    //         showProfileView();
    //     }
    // }

    public void showAddUserView(){

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(owner.getScene().getWindow());
        dialog.setTitle("Staff");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../scene/user/EditUser.fxml"));
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
        }else if(result.isPresent() && result.get() == closeButton){
            dialog.close();
        }
    }
}
