package com.example.cabinetmedical.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cabinetmedical.models.Appointment;
import com.example.cabinetmedical.models.Patient;

@Database(entities = {Patient.class, Appointment.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PatientDao patientDao();
    public abstract AppointmentDao appointmentDao();


    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "medical_clinic_db"
            ).build();
        }
        return INSTANCE;
    }
}
