package model;

public class Doctor extends Staff{
    private String expretise;

    public Doctor(){
        this("no expertise");
    }

    public Doctor(String expretise) {
        this.expretise = expretise;
    }

    public String getExpretise() {
        return expretise;
    }
    public void setExpretise(String expretise) {
        this.expretise = expretise;
    }

}
