package model;

import model.enums.MedicineType;

public class Medicine implements Comparable<Medicine> {
    
    private int id;
    private MedicineType type;
    private String name;

    public Medicine() {
    }

    public Medicine(String name, MedicineType type) {
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public MedicineType getType() {
        return type;
    }
    
    public void setType(MedicineType type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int compareTo(Medicine o) {
        return getName().compareTo(o.getName());
    }
    
    @Override
    public String toString() {
        return getId() + " " + getName() + " " + getType() + " ";
    }
    
}
