package model;

import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Priority;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Patient extends Person{
    String complaint;
    LocalDateTime appointment;
    EmergencyState emergencyState;
    Priority priority;
    BloodType bloodType;
    HashMap<Integer>
}
