package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

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

    public void showProfileDialog() {
        new ProfileViewBuilder(username, pharmacistPanel);
    }

    public void logout() {
        System.out.println("Logging out");
    }
}
