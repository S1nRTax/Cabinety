package com.example.cabinetmedical.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Room Entity class representing a patient in the medical cabinet application.
 */
@Entity(tableName = "patients")
@Getter
@Setter
public class Patient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "first_name")
    @NonNull
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "date_of_birth")
    private String dateOfBirth;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "blood_type")
    private String bloodType;

    @ColumnInfo(name = "allergies")
    private String allergies;

    @ColumnInfo(name = "medical_history")
    private String medicalHistory;

    /**
     * Constructor for creating a new patient
     */
    public Patient(@NonNull String firstName, String lastName, String dateOfBirth,
                   String gender, String phone, String email, String address,
                   String bloodType, String allergies, String medicalHistory) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
    }


    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}