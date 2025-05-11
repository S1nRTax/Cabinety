package com.example.cabinetmedical.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.data.repository.PatientRepository;

import java.util.List;

/**
 * ViewModel for Patient-related operations
 */
public class PatientViewModel extends AndroidViewModel {

    private final PatientRepository repository;
    private final LiveData<List<Patient>> allPatients;

    /**
     * Constructor initializes repository and cached data
     * @param application Application context
     */
    public PatientViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientRepository(application);
        allPatients = repository.getAllPatients();
    }

    /**
     * Insert a new patient
     * @param patient Patient to insert
     * @return ID of the inserted patient
     */
    public long insert(Patient patient) {
        return repository.insert(patient);
    }

    /**
     * Update an existing patient
     * @param patient Patient to update
     */
    public void update(Patient patient) {
        repository.update(patient);
    }

    /**
     * Delete a patient
     * @param patient Patient to delete
     */
    public void delete(Patient patient) {
        repository.delete(patient);
    }

    /**
     * Get all patients as LiveData
     * @return LiveData list of all patients
     */
    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }

    /**
     * Get a specific patient by ID
     * @param id Patient ID
     * @return LiveData containing the requested patient
     */
    public LiveData<Patient> getPatientById(long id) {
        return repository.getPatientById(id);
    }

    /**
     * Search patients by name
     * @param query Search query string
     * @return LiveData list of matching patients
     */
    public LiveData<List<Patient>> searchPatients(String query) {
        return repository.searchPatients(query);
    }


    public LiveData<Patient> getPatientByLastName(String lastName) {
        return repository.getPatientByLastName(lastName);
    }

    // In PatientViewModel.java, add these methods:
    public LiveData<Patient> loginPatient(String phoneNumber, String password) {
        return repository.loginPatient(phoneNumber, password);
    }

    public LiveData<Patient> getPatientByPhoneNumber(String phoneNumber) {
        return repository.getPatientByPhoneNumber(phoneNumber);
    }


}