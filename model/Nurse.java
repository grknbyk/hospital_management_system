package model;

public class Nurse extends Staff{
    
    private String workingArea;
    
    public Nurse() {
        this("no working area");
    }
    
    public Nurse(String workingArea) {
        this.workingArea = workingArea;
    }

    public String getWorkingArea() {
        return workingArea;
    }

    public void setWorkingArea(String workingArea) {
        this.workingArea = workingArea;
    }
}
