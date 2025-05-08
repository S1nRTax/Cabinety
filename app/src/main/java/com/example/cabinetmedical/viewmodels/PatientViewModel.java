package com.example.cabinetmedical.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cabinetmedical.database.PatientRepository;
import com.example.cabinetmedical.models.Patient;

import java.util.List;

import lombok.Getter;

public class PatientViewModel extends AndroidViewModel {
    private PatientRepository repository;
    @Getter
    private LiveData<List<Patient>> allPatients;

    public PatientViewModel(Application application) {
        super(application);
        repository = new PatientRepository(application);
        allPatients = repository.getAllPatients();
    }

    public void insert(Patient patient) {
        repository.insert(patient);
    }

    public void update(Patient patient) {
        repository.update(patient);
    }

    public void delete(Patient patient) {
        repository.delete(patient);
    }



}