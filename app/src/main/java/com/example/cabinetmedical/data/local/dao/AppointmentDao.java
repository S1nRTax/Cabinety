package com.example.cabinetmedical.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.TypeConverters;

import com.example.cabinetmedical.data.local.converters.DateConverter;
import com.example.cabinetmedical.data.local.entity.Appointment;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters(DateConverter.class)
public interface AppointmentDao {
    @Insert
    long insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY appointmentTime DESC")
    LiveData<List<Appointment>> getAppointmentsByPatient(long patientId);

    @Query("SELECT * FROM appointments WHERE appointmentTime BETWEEN :start AND :end ORDER BY appointmentTime")
    LiveData<List<Appointment>> getAppointmentsBetween(@TypeConverters(DateConverter.class) Date start,
                                                       @TypeConverters(DateConverter.class) Date end);

    @Query("SELECT * FROM appointments WHERE id = :id")
    LiveData<Appointment> getAppointmentById(long id);

    @Query("SELECT COUNT(*) FROM appointments WHERE appointmentTime = :time AND status != 'CANCELLED'")
    LiveData<Integer> countAppointmentsAtTime(@TypeConverters(DateConverter.class) Date time);

    @Delete
    void delete(Appointment appointment);
}