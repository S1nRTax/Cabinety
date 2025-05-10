package com.example.cabinetmedical.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.data.local.entity.Patient;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private List<Patient> patients;
    private OnAppointmentClickListener listener;

    public interface OnAppointmentClickListener {
        void onDeleteClick(Appointment appointment);
    }

    public AppointmentAdapter(OnAppointmentClickListener listener) {
        this.listener = listener;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        Patient patient = findPatientById(appointment.getPatientId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.tvPatientName.setText(patient != null ?
                patient.getFirstName() + " " + patient.getLastName() : "Unknown Patient");
        holder.tvDateTime.setText(dateFormat.format(appointment.getAppointmentTime()));
        holder.tvPurpose.setText("Purpose: " + appointment.getPurpose());
        holder.tvStatus.setText("Status: " + appointment.getStatus());

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(appointment);
            }
        });
    }

    private Patient findPatientById(long patientId) {
        if (patients == null) return null;
        for (Patient patient : patients) {
            if (patient.getId() == patientId) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDateTime, tvPurpose, tvStatus;
        Button btnDelete;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}