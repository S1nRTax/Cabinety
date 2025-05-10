package com.example.cabinetmedical.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patients = new ArrayList<>();
    private final OnPatientClickListener listener;

    public PatientAdapter(OnPatientClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient currentPatient = patients.get(position);
        holder.bind(currentPatient, listener);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private final TextView patientNameTextView;
        private final TextView patientInfoTextView;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.tvPatientName);
            patientInfoTextView = itemView.findViewById(R.id.tvPatientInfo);
        }

        public void bind(final Patient patient, final OnPatientClickListener listener) {
            // Set patient name
            String fullName = patient.getFirstName() + " " + patient.getLastName();
            patientNameTextView.setText(fullName);

            // Set additional patient info (could be date of birth, phone number, etc.)
            String additionalInfo = patient.getDateOfBirth() + " • " + patient.getPhone();
            patientInfoTextView.setText(additionalInfo);

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPatientClick(patient);
                }
            });
        }
    }

    // Interface for handling clicks
    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }
}