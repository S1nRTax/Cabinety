package com.example.cabinetmedical.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.cabinetmedical.models.Appointment;

import java.util.List;


public interface AppointmentDao {

    @Insert
    long insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("SELECT * FROM appointments")
    LiveData<List<Appointment>> getAllAppointments();

    @Query("SELECT * FROM appointments WHERE patientId = :id")
    LiveData<Appointment> getAppointmentById(int id);



}
