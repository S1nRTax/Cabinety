package com.example.cabinetmedical.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.data.repository.AppointmentRepository;

import java.util.Date;
import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private final AppointmentRepository repository;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application);
    }

    public long insert(Appointment appointment) {
        return repository.insert(appointment);
    }

    public void update(Appointment appointment) {
        repository.update(appointment);
    }

    public LiveData<List<Appointment>> getAppointmentsByPatient(long patientId) {
        return repository.getAppointmentsByPatient(patientId);
    }

    public LiveData<List<Appointment>> getAppointmentsBetween(Date start, Date end) {
        return repository.getAppointmentsBetween(start, end);
    }

    public LiveData<Appointment> getAppointmentById(long id) {
        return repository.getAppointmentById(id);
    }

    public LiveData<Integer> countAppointmentsAtTime(Date time) {
        return repository.countAppointmentsAtTime(time);
    }

    public void delete(Appointment appointment) {
        repository.delete(appointment);
    }
}