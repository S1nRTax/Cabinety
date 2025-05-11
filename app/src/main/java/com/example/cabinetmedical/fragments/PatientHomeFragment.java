package com.example.cabinetmedical.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.activities.LoginActivity;
import com.example.cabinetmedical.adapters.AppointmentPatientAdapter;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.viewmodels.AppointmentViewModel;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

import java.util.List;

public class PatientHomeFragment extends Fragment implements AppointmentPatientAdapter.OnAppointmentActionListener {

    private TextView tvPatientName;
    private RecyclerView rvAppointments;
    private AppointmentPatientAdapter appointmentAdapter;
    private AppointmentViewModel appointmentViewModel;
    private PatientViewModel patientViewModel;
    private long patientId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvPatientName = view.findViewById(R.id.tvPatientName);
        rvAppointments = view.findViewById(R.id.rvAppointments);
        ImageButton btnLogout = view.findViewById(R.id.btnLogout);

        // Get patient ID from shared preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        patientId = prefs.getLong("patientId", -1);

        // Initialize ViewModels
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        // Setup RecyclerView
        appointmentAdapter = new AppointmentPatientAdapter(this);
        rvAppointments.setAdapter(appointmentAdapter);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load patient data
        patientViewModel.getPatientById(patientId).observe(getViewLifecycleOwner(), patient -> {
            if (patient != null) {
                tvPatientName.setText(patient.getFirstName() + " " + patient.getLastName());
            }
        });

        // Load appointments
        loadAppointments();

        // Setup logout button
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadAppointments() {
        appointmentViewModel.getAppointmentsByPatient(patientId).observe(getViewLifecycleOwner(), appointments -> {
            if (appointments != null) {
                appointmentAdapter.setAppointments(appointments);
                scheduleNotifications(appointments);
            }
        });
    }

    private void scheduleNotifications(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            if ("CONFIRMED".equals(appointment.getStatus())) {
                scheduleNotificationForAppointment(appointment);
            }
        }
    }

    private void scheduleNotificationForAppointment(Appointment appointment) {
        // Notification scheduling logic (see next step)
    }

    private void logout() {
        SharedPreferences.Editor editor = requireActivity()
                .getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onAcceptAppointment(Appointment appointment) {
        appointment.setStatus("CONFIRMED");
        appointmentViewModel.update(appointment);
        scheduleNotificationForAppointment(appointment);
    }

    @Override
    public void onCancelAppointment(Appointment appointment) {
        appointment.setStatus("CANCELLED");
        appointmentViewModel.update(appointment);
        cancelScheduledNotification(appointment);
    }

    private void cancelScheduledNotification(Appointment appointment) {
        // Cancel notification logic
    }
}