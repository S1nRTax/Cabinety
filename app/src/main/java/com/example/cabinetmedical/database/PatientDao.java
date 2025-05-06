package com.example.cabinetmedical.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedical.models.Patient;

import java.util.List;

@Dao
public interface PatientDao {
    @Insert
    long insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("SELECT * FROM patients")
    LiveData<List<Patient>> getAllPatients();

    @Query("SELECT * FROM patients WHERE id = :id")
    LiveData<Patient> getPatientById(int id);

}