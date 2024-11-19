package org.company.insurance.enums;

public enum PropertyInsuranceType {
    SMALL(50000.0),
    MEDIUM(100000.0),
    LARGE(200000.0);

    private final double amount;

    PropertyInsuranceType(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public static PropertyInsuranceType getCoverageAmount(double houseSize) {
        if (houseSize < 100) {
            return SMALL;
        } else if (houseSize < 300) {
            return MEDIUM;
        } else {
            return LARGE;
        }
    }
}
