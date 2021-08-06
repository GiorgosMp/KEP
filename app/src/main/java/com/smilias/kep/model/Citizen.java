package com.smilias.kep.model;

import java.util.Date;

public class Citizen {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private String birthDate;
    private String id;
    private String amka;
    private String taxNumber;
    private String address;
    private String email;
    private String username;

    public Citizen(String username, String firstName, String lastName, String fatherName, String motherName, String birthDate,
                   String id, String amka, String taxNumber, String address, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.birthDate = birthDate;
        this.id = id;
        this.amka = amka;
        this.taxNumber = taxNumber;
        this.address = address;
        this.email = email;
    }

    public Citizen() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getAmka() {
        return amka;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", id='" + id + '\'' +
                ", amka='" + amka + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
