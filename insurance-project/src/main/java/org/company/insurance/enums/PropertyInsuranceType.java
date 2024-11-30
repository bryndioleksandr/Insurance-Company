package org.company.insurance.enums;

public enum PropertyInsuranceType {
    SMALL(50000.0),
    MEDIUM(100000.0),
    LARGE(200000.0);

    private final Double amount;

    PropertyInsuranceType(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public static PropertyInsuranceType getCoverageAmount(Double houseSize) {
        if (houseSize < 100) {
            return SMALL;
        } else if (houseSize < 300) {
            return MEDIUM;
        } else {
            return LARGE;
        }
    }
}
