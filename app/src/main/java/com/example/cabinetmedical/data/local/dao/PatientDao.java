package com.example.cabinetmedical.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cabinetmedical.data.local.entity.Patient;

import java.util.List;

/**
 * Data Access Object for the Patient entity
 */
@Dao
public interface PatientDao {
    
    /**
     * Insert a new patient into the database
     * @param patient the patient to insert
     * @return the ID of the newly inserted patient
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Patient patient);
    
    /**
     * Update an existing patient in the database
     * @param patient the patient to update
     */
    @Update
    void update(Patient patient);
    
    /**
     * Delete a patient from the database
     * @param patient the patient to delete
     */
    @Delete
    void delete(Patient patient);
    
    /**
     * Get all patients from the database
     * @return LiveData list of all patients
     */
    @Query("SELECT * FROM patients ORDER BY last_name, first_name")
    LiveData<List<Patient>> getAllPatients();
    
    /**
     * Get a patient by ID
     * @param id the patient ID
     * @return the patient with the specified ID
     */
    @Query("SELECT * FROM patients WHERE id = :id")
    LiveData<Patient> getPatientById(long id);
    
    /**
     * Search patients by name (first name or last name)
     * @param searchQuery the search query
     * @return list of patients matching the search query
     */
    @Query("SELECT * FROM patients WHERE first_name LIKE '%' || :searchQuery || '%' " +
           "OR last_name LIKE '%' || :searchQuery || '%' " +
           "ORDER BY last_name, first_name")
    LiveData<List<Patient>> searchPatients(String searchQuery);
}