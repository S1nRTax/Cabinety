package com.example.cabinetmedical.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "appointments")
@Getter
@Setter
@AllArgsConstructor
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int patientId;

    private long dateTime;

    private String status; // "confirmed", "cancelled", "completed", "pending"
}
