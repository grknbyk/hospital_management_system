package ui.controllers;

public class ReceptionistController {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showProfileDialog() {
        System.out.println("Showing profile dialog");
    }

    public void logout() {
        System.out.println("Logging out");
    }
}
