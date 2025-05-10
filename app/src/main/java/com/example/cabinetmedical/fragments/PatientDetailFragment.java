package com.example.cabinetmedical.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

public class PatientDetailFragment extends Fragment {

    private static final String TAG = "PatientDetailFragment";
    private PatientViewModel patientViewModel;
    private TextView tvName, tvDob, tvGender, tvPhone, tvEmail, tvAddress, tvBloodType, tvAllergies, tvMedicalHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get ViewModel
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
        Log.d(TAG, "ViewModel initialized");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.fragment_patient_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");

        // Initialize views
        initializeViews(view);

        // Get patient ID from arguments
        if (getArguments() != null) {
            long patientId = getArguments().getLong("patientId", -1);
            Log.d(TAG, "Patient ID from arguments: " + patientId);

            if (patientId != -1) {
                loadPatientData(patientId);
            } else {
                Log.e(TAG, "Invalid patient ID");
                Toast.makeText(requireContext(), "Error: Invalid patient ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "No arguments provided");
            Toast.makeText(requireContext(), "Error: No patient selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews(View view) {
        tvName = view.findViewById(R.id.tvPatientName);
        tvDob = view.findViewById(R.id.tvDob);
        tvGender = view.findViewById(R.id.tvGender);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvBloodType = view.findViewById(R.id.tvBloodType);
        tvAllergies = view.findViewById(R.id.tvAllergies);
        tvMedicalHistory = view.findViewById(R.id.tvMedicalHistory);

        // Set default placeholder text
        setPlaceholderText();
    }

    private void setPlaceholderText() {
        String loadingText = "Loading...";
        tvName.setText(loadingText);
        tvDob.setText(loadingText);
        tvGender.setText(loadingText);
        tvPhone.setText(loadingText);
        tvEmail.setText(loadingText);
        tvAddress.setText(loadingText);
        tvBloodType.setText(loadingText);
        tvAllergies.setText(loadingText);
        tvMedicalHistory.setText(loadingText);
    }

    private void loadPatientData(long patientId) {
        patientViewModel.getPatientById(patientId).observe(getViewLifecycleOwner(), patient -> {
            if (patient != null) {
                Log.d(TAG, "Patient data received: " + patient.getFirstName() + " " + patient.getLastName());
                displayPatientDetails(patient);
            } else {
                Log.e(TAG, "Patient object is null");
                Toast.makeText(requireContext(), "Error: Patient not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPatientDetails(Patient patient) {
        try {
            // Format name
            String fullName = patient.getFirstName() + " " + patient.getLastName();
            tvName.setText(fullName);

            // Format date of birth
            String dob = patient.getDateOfBirth();
            tvDob.setText(getString(R.string.date_of_birth_format, dob));

            // Gender
            tvGender.setText(getString(R.string.gender_format, patient.getGender()));

            // Contact info
            tvPhone.setText(patient.getPhone());
            tvEmail.setText(patient.getEmail());
            tvAddress.setText(patient.getAddress());

            // Medical info
            tvBloodType.setText(getString(R.string.blood_type_format, patient.getBloodType()));

            // Allergies - Handle possible null values
            String allergies = patient.getAllergies();
            tvAllergies.setText(allergies != null && !allergies.isEmpty() ?
                    allergies : getString(R.string.no_allergies));

            // Medical history - Handle possible null values
            String medicalHistory = patient.getMedicalHistory();
            tvMedicalHistory.setText(medicalHistory != null && !medicalHistory.isEmpty() ?
                    medicalHistory : getString(R.string.no_medical_history));

            Log.d(TAG, "Patient details displayed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error displaying patient details", e);
            Toast.makeText(requireContext(), "Error displaying patient details", Toast.LENGTH_SHORT).show();
        }
    }
}