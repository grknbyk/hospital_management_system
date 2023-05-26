package model;

import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Priority;

import java.time.LocalDateTime;

public class Patient extends Person{
    String complaint;
    LocalDateTime appointment;
    EmergencyState emergencyState;
    Priority priority;
    BloodType bloodType;

    public Patient(){

    }

    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public LocalDateTime getAppointment() {
        return appointment;
    }
    public void setAppointment(LocalDateTime appointment) {
        this.appointment = appointment;
    }
    public EmergencyState getEmergencyState() {
        return emergencyState;
    }
    public void setEmergencyState(EmergencyState emergencyState) {
        this.emergencyState = emergencyState;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public BloodType getBloodType() {
        return bloodType;
    }
    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}
