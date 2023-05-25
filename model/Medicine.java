package model;

import model.enums.MedicineType;

public class Medicine implements Comparable<Medicine> {
    MedicineType type;
    String name;

    @Override
    public int hashCode() {
        return name.hashCode();
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
