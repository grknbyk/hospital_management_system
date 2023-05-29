package model.enums;

public enum WorkingArea {
    INTENSIVE_CARE_UNIT("Intensive Care Unit"),
    EMERGENCY_DEPARTMENT("Emergency Department"),
    OPERATING_ROOM("Operating Room"),
    MEDICAL_SURGICAL_WARD("Medical-Surgical Ward");

    private final String value;

    WorkingArea(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

