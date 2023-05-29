package model.enums;

import java.util.Locale;

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
        return n.substring(0, 1).toUpperCase(Locale.ROOT) + n.substring(1).toLowerCase(Locale.ROOT);
    }
}
