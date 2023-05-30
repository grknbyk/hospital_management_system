package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;
import model.enums.*;

import java.awt.Label;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import database.Datasource;

public class RegisterPatientController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField complaintTextField;

    @FXML
    private Spinner<Integer> ageSpinner;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    private ChoiceBox<Gender> genderChoiceBox;

    @FXML
    private ComboBox<BloodType> bloodTypeComboBox;

    @FXML
    private ComboBox<EmergencyState> emergencyStateComboBox;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private ComboBox<Expertise> departmentComboBox;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private ComboBox<WorkingArea> workingAreaComboBox;

    @FXML
    private DatePicker appointmentDatePicker;

    @FXML
    private Button todayButton;

    private HashMap<Expertise, ArrayList<Person>> doctors = new HashMap<>();

    @FXML
    private ImageView imageView;




    @FXML
    private void todayButtonClicked() {
        appointmentDatePicker.setValue(LocalDate.now());
    }

    public void initialize() {
        imageView.setImage(new Image("ui/imgs/patient.png"));
        complaintTextField = new TextField();
        ageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 18));
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 10));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 30, 5));
        genderChoiceBox.getItems().clear();
        genderChoiceBox.getItems().addAll(Gender.values());
        bloodTypeComboBox.getItems().clear();
        bloodTypeComboBox.getItems().addAll(BloodType.values());
        emergencyStateComboBox.getItems().clear();
        emergencyStateComboBox.getItems().addAll(EmergencyState.values());
        priorityComboBox.getItems().clear();
        priorityComboBox.getItems().addAll(Priority.values());
        workingAreaComboBox.getItems().clear();
        workingAreaComboBox.getItems().addAll(WorkingArea.values());

        departmentComboBox.getItems().clear();
        doctorComboBox.getItems().clear();
        for (Expertise expertise : Expertise.values()) {
            departmentComboBox.getItems().add(expertise);
            ArrayList<Person> persons = Datasource.getInstance().queryDoctors(expertise);
            doctors.put(expertise, persons);
        }

        departmentComboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            doctorComboBox.getItems().clear();
            if (newValue != null) {
                ArrayList<Person> persons = doctors.get(newValue);
                for (Person person : persons) {
                    doctorComboBox.getItems().add(person.toString());
                }
                doctorComboBox.getSelectionModel().select(0);
            }

        });

    }

    public void assignPatient(Patient patient) {
        nameTextField.setText(patient.getName());
        nameTextField.editableProperty().setValue(false);
        surnameTextField.setText(patient.getSurname());
        surnameTextField.editableProperty().setValue(false);
        genderChoiceBox.getItems().clear();
        genderChoiceBox.setValue(patient.getGender());
        int age = patient.getAge();
        ageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(age, age));
        ageSpinner.getValueFactory().setValue(age);
        ageSpinner.editableProperty().setValue(false);
        bloodTypeComboBox.getItems().clear();
        bloodTypeComboBox.setValue(patient.getBloodType());
        bloodTypeComboBox.setEditable(false);
        emergencyStateComboBox.getItems().clear();
        emergencyStateComboBox.setValue(patient.getEmergencyState());
        emergencyStateComboBox.setEditable(false);
        priorityComboBox.getItems().clear();
        priorityComboBox.setValue(patient.getPriority());
        priorityComboBox.setEditable(false);
        complaintTextField.setText(patient.getComplaint() == null ? "" : patient.getComplaint());
        complaintTextField.editableProperty().setValue(false);
    }

    public void registerPatient() {
        nameTextField.editableProperty().setValue(true);
        surnameTextField.editableProperty().setValue(true);
        genderChoiceBox.getItems().clear();
        genderChoiceBox.setValue(null);
        complaintTextField.setText("");
        complaintTextField.editableProperty().setValue(true);
        initialize();
    }

    public Patient returnPatient(Patient patient) {
        if(patient == null) patient = new Patient();
        patient.setReceiptId(-1);
        try {
            if (nameTextField.getText().trim().isEmpty()) {
                showInvalidInputAlert("Name");
                return null;
            }
            patient.setName(nameTextField.getText().trim());
        } catch (Exception e) {
            showInvalidInputAlert("Name");
            return null;
        }

        try {
            if (surnameTextField.getText().trim().isEmpty()) {
                showInvalidInputAlert("Surname");
                return null;
            }
            patient.setSurname(surnameTextField.getText().trim());
        } catch (Exception e) {
            showInvalidInputAlert("Surname");
            return null;
        }

        try {
            if (complaintTextField.getText().trim().isEmpty()) {
                patient.setComplaint("");
                // System.out.println(complaintTextField.getText());
                // showInvalidInputAlert("Complaint");
                // return null;
            }
            patient.setComplaint(complaintTextField.getText().trim());
        } catch (Exception e) {
            showInvalidInputAlert("Complaint");
            return null;
        }

        try {
            Gender gender = genderChoiceBox.getValue();
            if (gender == null) {
                showInvalidInputAlert("Gender");
                return null;
            }
            patient.setGender(gender);
        } catch (Exception e) {
            showInvalidInputAlert("Gender");
            return null;
        }

        try {
            int age = ageSpinner.getValue();
            patient.setAge(age);
        } catch (Exception e) {
            showInvalidInputAlert("Age");
            return null;
        }

        try {
            BloodType bloodType = bloodTypeComboBox.getValue();
            if (bloodType == null) {
                showInvalidInputAlert("Blood Type");
                return null;
            }
            patient.setBloodType(bloodType);
        } catch (Exception e) {
            showInvalidInputAlert("Blood Type");
            return null;
        }

        try {
            EmergencyState emergencyState = emergencyStateComboBox.getValue();
            if (emergencyState == null) {
                showInvalidInputAlert("Emergency State");
                return null;
            }
            patient.setEmergencyState(emergencyState);
        } catch (Exception e) {
            showInvalidInputAlert("Emergency State");
            return null;
        }

        try {
            Priority priority = priorityComboBox.getValue();
            if (priority == null) {
                showInvalidInputAlert("Priority");
                return null;
            }
            patient.setPriority(priority);
        } catch (Exception e) {
            showInvalidInputAlert("Priority");
            return null;
        }

        try {
            Expertise expertise = departmentComboBox.getValue();
            if (expertise == null) {
                showInvalidInputAlert("Department");
                return null;
            }
            try {
                String doctorName = doctorComboBox.getValue();
                if (doctorName == null) {
                    showInvalidInputAlert("Doctor");
                    return null;
                }
                for (Person person : doctors.get(expertise)) {
                    if (person.getName().equals(doctorName)) {
                        patient.setDoctorId(person.getId());
                        break;
                    }
                }
            } catch (Exception e) {
                showInvalidInputAlert("Doctor");
                return null;
            }

        } catch (Exception e) {
            showInvalidInputAlert("Department");
            return null;
        }

        try {
            LocalDate date = appointmentDatePicker.getValue(); // current LocalDate object
            int hour, minutes;
            try {
                hour = hourSpinner.getValue(); // hour value
                try {                    
                    minutes = minuteSpinner.getValue(); // minutes value
                } catch (Exception e) {
                    showInvalidInputAlert("Minutes");
                    return null;
                }
            } catch (Exception e) {
                showInvalidInputAlert("Hour");
                return null;
            }
            // Create LocalDateTime object using the hour, minutes, and LocalDate
            LocalDateTime localDateTime = date.atTime(hour, minutes);
            patient.setAppointment(localDateTime);
        } catch (Exception e) {
            showInvalidInputAlert("Date");
            return null;
        }

        return patient;
    }

    public void showInvalidInputAlert(String field) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Input: " + field);
        alert.setContentText("Please enter a valid input for " + field);
        alert.showAndWait();
    }
}
