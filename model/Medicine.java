package model;

import model.enums.MedicineType;

public class Medicine implements Comparable<Medicine> {
    int id;
    MedicineType type;
    String name;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
