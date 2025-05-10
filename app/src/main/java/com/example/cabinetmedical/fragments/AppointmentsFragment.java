package com.example.cabinetmedical.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.adapters.AppointmentAdapter;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.AppointmentViewModel;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

import java.util.List;

public class AppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentClickListener {

    private AppointmentViewModel appointmentViewModel;
    private PatientViewModel patientViewModel;
    private AppointmentAdapter appointmentAdapter;
    private RecyclerView recyclerView;
    private TextView tvNoAppointments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        recyclerView = view.findViewById(R.id.appointmentsRecyclerView);
        tvNoAppointments = view.findViewById(R.id.tvNoAppointments);

        // Setup RecyclerView
        appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                LinearLayoutManager.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Observe appointments
        appointmentViewModel.getAppointmentsBetween(
                new java.util.Date(0), // Very old date
                new java.util.Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)) // 1 year from now
        ).observe(getViewLifecycleOwner(), appointments -> {
            if (appointments != null && !appointments.isEmpty()) {
                appointmentAdapter.setAppointments(appointments);
                tvNoAppointments.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvNoAppointments.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        // Observe patients (needed to display patient names)
        patientViewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
            if (patients != null) {
                appointmentAdapter.setPatients(patients);
            }
        });
    }

    @Override
    public void onDeleteClick(Appointment appointment) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    appointmentViewModel.delete(appointment);
                    Toast.makeText(requireContext(), "Appointment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}