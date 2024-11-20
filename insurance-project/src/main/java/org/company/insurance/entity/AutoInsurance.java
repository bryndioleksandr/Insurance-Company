package org.company.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.insurance.enums.AutoInsuranceType;
import org.company.insurance.enums.InsuranceStatus;
import org.company.insurance.enums.VehicleType;

@Entity
@Table(name = "auto_insurances")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AutoInsurance extends Insurance {
    @Column(name = "engine_capacity")
    private double engineCapacity;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "plate")
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type")
    private AutoInsuranceType insuranceType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "policy_id")
    private InsurancePolicy insurancePolicy;

    public double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public AutoInsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(AutoInsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }
}
