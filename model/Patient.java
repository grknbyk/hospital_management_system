package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Priority;

import java.time.LocalDateTime;

public class Patient extends Person{
    private SimpleStringProperty complaint = new SimpleStringProperty("");
    private ObjectProperty<LocalDateTime> appointment = new SimpleObjectProperty<>();
    private ObjectProperty<EmergencyState> emergencyState = new SimpleObjectProperty<>();
    private ObjectProperty<Priority> priority = new SimpleObjectProperty<>();
    private ObjectProperty<BloodType> bloodType = new SimpleObjectProperty<>();
    private ObjectProperty<Integer> doctorId = new SimpleObjectProperty<>();
    private ObjectProperty<Integer> receiptId = new SimpleObjectProperty<>();
    private SimpleStringProperty hour = new SimpleStringProperty("");
    public Patient(){

    }

    public String getHour() {
        return hour.get();
    }

    public SimpleStringProperty hourProperty() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour.set(hour);
    }

    public String getComplaint() {
        return complaint.get();
    }
    public void setComplaint(String complaint) {
        this.complaint.set(complaint);
    }
    public LocalDateTime getAppointment() {
        return appointment.get();
    }
    public void setAppointment(LocalDateTime appointment) {
        this.appointment.set(appointment);
        try {
            this.hour.set(appointment.getHour()+":"+appointment.getMinute());
        }catch (Exception e){

        }
    }
    public EmergencyState getEmergencyState() {
        return emergencyState.get();
    }
    public void setEmergencyState(EmergencyState emergencyState) {
        this.emergencyState.set(emergencyState);
    }
    public Priority getPriority() {
        return priority.get();
    }
    public void setPriority(Priority priority) {
        this.priority.set(priority);
    }
    public BloodType getBloodType() {
        return bloodType.get();
    }
    public void setBloodType(BloodType bloodType) {
        this.bloodType.set(bloodType);
    }

    public int getDoctorId() {
        return doctorId.get();
    }

    public void setDoctorId(int doctorId) {
        this.doctorId.set(doctorId);
    }

    public int getReceiptId() {
        return receiptId.get();
    }

    public void setReceiptId(int receiptId) {
        this.receiptId.set(receiptId);
    }
}
