package com.example.cabinetmedical.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cabinetmedical.data.local.dao.PatientDao;
import com.example.cabinetmedical.data.local.database.AppDatabase;
import com.example.cabinetmedical.data.local.entity.Patient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class that abstracts access to the Patient data sources
 */
public class PatientRepository {
    
    private final PatientDao patientDao;
    private final LiveData<List<Patient>> allPatients;
    private final ExecutorService executorService;
    
    /**
     * Constructor initializes database and DAO
     * @param application Application context
     */
    public PatientRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.patientDao = database.patientDao();
        this.allPatients = patientDao.getAllPatients();
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Insert a patient into the database
     * @param patient Patient to insert
     * @return ID of the inserted patient
     */
    public long insert(Patient patient) {
        final long[] patientId = {0};
        try {
            executorService.submit(() -> {
                patientId[0] = patientDao.insert(patient);
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientId[0];
    }
    
    /**
     * Update an existing patient
     * @param patient Patient to update
     */
    public void update(Patient patient) {
        executorService.execute(() -> patientDao.update(patient));
    }
    
    /**
     * Delete a patient
     * @param patient Patient to delete
     */
    public void delete(Patient patient) {
        executorService.execute(() -> patientDao.delete(patient));
    }
    
    /**
     * Get all patients as LiveData
     * @return LiveData list of all patients
     */
    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }
    
    /**
     * Get patient by ID
     * @param id Patient ID
     * @return LiveData containing the requested patient
     */
    public LiveData<Patient> getPatientById(long id) {
        return patientDao.getPatientById(id);
    }
    
    /**
     * Search patients by name
     * @param query Search query (name)
     * @return LiveData list of matching patients
     */
    public LiveData<List<Patient>> searchPatients(String query) {
        return patientDao.searchPatients(query);
    }


    public LiveData<Patient> getPatientByLastName(String lastName) {
        return patientDao.getPatientByLastName(lastName);
    }
}