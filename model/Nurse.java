package model;

import javafx.beans.property.SimpleStringProperty;

public class Nurse extends Staff{
    
    private SimpleStringProperty workingArea = new SimpleStringProperty("");
    
    public Nurse() {
        this("no working area");
    }
    
    public Nurse(String workingArea) {
        this.workingArea.set(workingArea);
    }

    public String getWorkingArea() {
        return workingArea.get();
    }

    public void setWorkingArea(String workingArea) {
        this.workingArea.set(workingArea);
    }
}
