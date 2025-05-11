package com.example.cabinetmedical.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.activities.LoginActivity;
import com.example.cabinetmedical.adapters.AppointmentPatientAdapter;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.utils.NotificationHelper;
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

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

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
                for (Appointment appointment : appointments) {
                    if ("CONFIRMED".equals(appointment.getStatus())) {
                        scheduleNotificationForAppointment(appointment);
                    }
                }
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
        long appointmentTime = appointment.getAppointmentTime().getTime();

        // 1 day before
        if (appointmentTime - System.currentTimeMillis() > 24 * 60 * 60 * 1000) {
            scheduleSingleNotification(appointment, appointmentTime - (24 * 60 * 60 * 1000),
                    "Reminder: Appointment tomorrow");
        }

        // 1 hour before
        scheduleSingleNotification(appointment, appointmentTime - (60 * 60 * 1000),
                "Reminder: Appointment in 1 hour");

        // 15 minutes before
        scheduleSingleNotification(appointment, appointmentTime - (15 * 60 * 1000),
                "Reminder: Appointment soon");
    }

    private void scheduleSingleNotification(Appointment appointment, long triggerAtMillis, String title) {
        if (triggerAtMillis > System.currentTimeMillis()) {
            new NotificationHelper(requireContext())
                    .scheduleNotification(appointment, triggerAtMillis, title);
        }
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
    public void onResume() {
        super.onResume();
        checkAndRequestNotificationPermission();
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now schedule notifications
            } else {
                Toast.makeText(requireContext(),
                        "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAcceptAppointment(Appointment appointment) {
        appointment.setStatus(Appointment.STATUS_CONFIRMED);
        appointmentViewModel.update(appointment);
        scheduleNotificationForAppointment(appointment);
        Toast.makeText(requireContext(), "Appointment confirmed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRejectAppointment(Appointment appointment) {
        appointment.setStatus(Appointment.STATUS_REJECTED);
        appointmentViewModel.update(appointment);
        cancelScheduledNotification(appointment);
        Toast.makeText(requireContext(), "Appointment rejected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelAppointment(Appointment appointment) {
        appointment.setStatus(Appointment.STATUS_CANCELLED);
        appointmentViewModel.update(appointment);
        cancelScheduledNotification(appointment);
        Toast.makeText(requireContext(), "Appointment cancelled", Toast.LENGTH_SHORT).show();
    }

    private void cancelScheduledNotification(Appointment appointment) {
        new NotificationHelper(requireContext())
                .cancelNotification(appointment.getId());
    }
}