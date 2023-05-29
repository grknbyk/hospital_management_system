package model.enums;

public enum Expertise {
    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    ORTHOPEDIC_SURGERY("Orthopedic Surgery"),
    ONCOLOGY("Oncology"),
    PEDIATRICS("Pediatrics"),
    PSYCHIATRY("Psychiatry"),
    DERMATOLOGY("Dermatology"),
    OPHTHALMOLOGY("Ophthalmology"),
    ANESTHESIOLOGY("Anesthesiology");

    private final String value;

    Expertise(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

