package com.medilabo.prediction.domain;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class Patient {

    private int idPatient;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    private String address;
    private String phoneNumber;

    public Patient(String firstName, String lastName, Date birthDate, Gender gender, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Patient() {}

    public int getAge() {
        LocalDate localBirthDate = birthDate.toLocalDate();
        Period period = Period.between(localBirthDate, LocalDate.now());
        return period.getYears();
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}