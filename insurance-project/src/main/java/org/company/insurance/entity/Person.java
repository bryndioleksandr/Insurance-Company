package org.company.insurance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Date;


@MappedSuperclass
@Getter
@Setter

public class Person extends BaseEntity{

//    @Column(name = "email")
//    private String email;
//
//    private String emailVerificationCode;
//
//    private LocalDateTime emailVerificationExpiry;
//
//    private boolean emailVerified = false;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

//    public String getEmailVerificationCode() {
//        return emailVerificationCode;
//    }
//
//    public void setEmailVerificationCode(String emailVerificationCode) {
//        this.emailVerificationCode = emailVerificationCode;
//    }
//
//    public LocalDateTime getEmailVerificationExpiry() {
//        return emailVerificationExpiry;
//    }
//
//    public void setEmailVerificationExpiry(LocalDateTime emailVerificationExpiry) {
//        this.emailVerificationExpiry = emailVerificationExpiry;
//    }
//
//    public boolean isEmailVerified() {
//        return emailVerified;
//    }
//
//    public void setEmailVerified(boolean emailVerified) {
//        this.emailVerified = emailVerified;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
