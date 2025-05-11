package com.example.cabinetmedical.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.cabinetmedical.data.local.converters.DateConverter;

import java.util.Date;


@Entity(tableName = "appointments",
        foreignKeys = @ForeignKey(entity = Patient.class,
                parentColumns = "id",
                childColumns = "patientId",
                onDelete = ForeignKey.CASCADE))
@TypeConverters(DateConverter.class)
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long patientId;
    private Date appointmentTime;
    private String purpose;
    private String status; // "CONFIRMED", "FINISHED", "CANCELLED", "ON_HOLD"
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    private String notes;

    // Constructors, getters and setters
    public Appointment(long patientId, Date appointmentTime, String purpose, String status) {
        this.patientId = patientId;
        this.appointmentTime = appointmentTime;
        this.purpose = purpose;
        this.status = status;
    }

    // Getters and setters for all fields
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getPatientId() { return patientId; }
    public void setPatientId(long patientId) { this.patientId = patientId; }
    public Date getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Date appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}