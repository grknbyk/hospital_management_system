package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfileController {

    @FXML
    private Label AgeField;

    @FXML
    private Label GenderField;

    @FXML
    private Label SurnameField;

    @FXML
    private Label addressField;

    @FXML
    private Label emailField;

    @FXML
    private ImageView imageField;

    @FXML
    private Label nameField;

    @FXML
    private Label phoneField;

    @FXML
    private Label statusField;

    public void initialize(){
        imageField.setImage(new Image("ui/imgs/default_person.png"));
    }

}
