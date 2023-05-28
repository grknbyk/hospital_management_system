package model.enums;

public enum MedicineType {
    //https://www.gosh.nhs.uk/conditions-and-treatments/medicines-information/types-medicines/
    LIQUID,
    TABLET,
    CAPSULES,
    TOPICAL,
    SUPPOSITORIES,
    DROPS,
    INHALERS,
    INJECTIONS,
    PATCHES,
    BUCCAL;

    public String toString() {
        String n = name();
        return n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();
    }
}
