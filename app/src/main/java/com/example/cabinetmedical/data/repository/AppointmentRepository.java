package com.example.cabinetmedical.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cabinetmedical.data.local.dao.AppointmentDao;
import com.example.cabinetmedical.data.local.database.AppDatabase;
import com.example.cabinetmedical.data.local.entity.Appointment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentRepository {
    private final AppointmentDao appointmentDao;
    private final ExecutorService executorService;

    public AppointmentRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.appointmentDao = database.appointmentDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public long insert(Appointment appointment) {
        final long[] id = {0};
        executorService.execute(() -> {
            id[0] = appointmentDao.insert(appointment);
        });
        return id[0];
    }

    public void update(Appointment appointment) {
        executorService.execute(() -> appointmentDao.update(appointment));
    }

    public LiveData<List<Appointment>> getAppointmentsByPatient(long patientId) {
        return appointmentDao.getAppointmentsByPatient(patientId);
    }

    public LiveData<List<Appointment>> getAppointmentsBetween(Date start, Date end) {
        return appointmentDao.getAppointmentsBetween(start, end);
    }

    public LiveData<Appointment> getAppointmentById(long id) {
        return appointmentDao.getAppointmentById(id);
    }

    public LiveData<Integer> countAppointmentsAtTime(Date time) {
        return appointmentDao.countAppointmentsAtTime(time);
    }

    public void delete(Appointment appointment) {
        executorService.execute(() -> appointmentDao.delete(appointment));
    }
}