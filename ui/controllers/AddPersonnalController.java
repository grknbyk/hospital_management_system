package ui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Contact;
import model.Doctor;
import model.Nurse;
import model.Staff;
import model.enums.Expertise;
import model.enums.Gender;
import model.enums.Status;
import model.enums.WorkingArea;

public class AddPersonnalController {
    @FXML
    private ImageView imageField;

    @FXML
    private Spinner<Integer> ageSpinner;

    @FXML
    private ChoiceBox<Gender> genderChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private ChoiceBox<Status> statusChoiceBox;

    @FXML
    private ChoiceBox<String> statusChoiceBox2;

    @FXML
    private Label workingAreaLabel;

    @FXML
    private Label expertiseLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    public void initialize() {
        Image menuImg = new Image("ui/imgs/default_person.png");
        ImageView imageView = new ImageView(menuImg);
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        imageField.setImage(menuImg);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(18, 100);
        ageSpinner.setValueFactory(valueFactory);
        ageSpinner.getValueFactory().setValue(18);

        genderChoiceBox.getItems().addAll(Gender.values());
        genderChoiceBox.setValue(Gender.MALE);

        workingAreaLabel.setVisible(false);
        expertiseLabel.setVisible(false);
        statusChoiceBox2.setVisible(false);

        
        statusChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == Status.DOCTOR) {
                workingAreaLabel.setVisible(false);
                expertiseLabel.setVisible(true);
                statusChoiceBox2.getItems().clear();
                for (Expertise expertise : Expertise.values()) {
                    statusChoiceBox2.getItems().add(expertise.toString());
                }
                statusChoiceBox2.setVisible(true);
            } else if(newValue == Status.NURSE){
                expertiseLabel.setVisible(false);
                workingAreaLabel.setVisible(true);
                statusChoiceBox2.getItems().clear();
                for (WorkingArea workingArea : WorkingArea.values()) {
                    statusChoiceBox2.getItems().add(workingArea.toString());
                }
                statusChoiceBox2.setVisible(true);
            } else{
                workingAreaLabel.setVisible(false);
                expertiseLabel.setVisible(false);
                statusChoiceBox2.setVisible(false);
            }
        });

        statusChoiceBox.getItems().addAll(Status.values());
        statusChoiceBox.setValue(Status.DOCTOR);
    }

    public Staff returnStaff(){
        Staff staff;
        Status status = statusChoiceBox.getValue();
        if(status == Status.DOCTOR){
            Doctor doctor = new Doctor();
            doctor.setExpertise(statusChoiceBox2.getValue());
            staff = doctor;
        } else if(status == Status.NURSE){
            Nurse nurse = new Nurse();
            nurse.setWorkingArea(statusChoiceBox2.getValue());
            staff = nurse;
        }
        else{
            staff = new Staff(){};
        }

        staff.setName(nameTextField.getText());
        staff.setSurname(surnameTextField.getText());
        staff.setGender(genderChoiceBox.getValue());
        staff.setAge(ageSpinner.getValue());
        Contact contact = new Contact();
        contact.setAddress(addressTextField.getText());
        contact.setEmail(emailTextField.getText());
        contact.setPhone(phoneTextField.getText());
        staff.setContact(contact);
        staff.setStatus(statusChoiceBox.getValue());
        staff.setUsername(usernameTextField.getText());
        staff.setPassword(passwordTextField.getText());
        
        return staff;
    }

}
