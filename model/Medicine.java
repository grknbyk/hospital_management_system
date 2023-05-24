package model;

import model.enums.MedicineType;

public class Medicine {
    MedicineType type;
    String name;

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
