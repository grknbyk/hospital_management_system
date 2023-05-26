package model;

import javafx.beans.property.SimpleStringProperty;

public class Doctor extends Staff{
    private SimpleStringProperty expertise = new SimpleStringProperty("");

    public Doctor(){
        this("no expertise");
    }

    public Doctor(String expertise) {
        this.expertise.set(expertise);
    }

    public String getExpertise() {
        return expertise.get();
    }
    public void setExpertise(String expertise) {
        this.expertise.set(expertise);
    }

}
