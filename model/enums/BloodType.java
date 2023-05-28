package model.enums;

/**
 * Represents blood type of patient.
 * P for positive, N for negative, Z for 0.
 */
public enum BloodType {
    AP("A+"),
    AN("A-"),
    BP("B+"),
    BN("B-"),
    ABP("AB+"),
    ABN("AB-"),
    ZP("0+"),
    ZN("0-");

    private final String _name;

    BloodType(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }
}
