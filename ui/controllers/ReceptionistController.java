package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class ReceptionistController {
    String username;

    @FXML
    private BorderPane receptionistPanel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showProfileDialog() {
        new ProfileViewBuilder(username, receptionistPanel);
    }

    public void logout() {
        System.out.println("Logging out");
    }
}
