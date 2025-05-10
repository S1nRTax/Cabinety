package com.example.cabinetmedical.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.PatientViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddPatientFragment extends Fragment {

    private TextInputEditText etFirstName, etLastName, etDob, etPhone, etEmail, etAddress, etAllergies, etMedicalHistory;
    private AutoCompleteTextView actvGender, actvBloodType;
    private MaterialButton btnSubmit;
    private PatientViewModel patientViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_patient, container, false);

        // Initialize views
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etDob = view.findViewById(R.id.etDob);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etAddress = view.findViewById(R.id.etAddress);
        etAllergies = view.findViewById(R.id.etAllergies);
        etMedicalHistory = view.findViewById(R.id.etMedicalHistory);
        actvGender = view.findViewById(R.id.actvGender);
        actvBloodType = view.findViewById(R.id.actvBloodType);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        // Setup dropdowns
        setupGenderDropdown();
        setupBloodTypeDropdown();

        // Date picker for DOB
        etDob.setOnClickListener(v -> showDatePickerDialog());

        // Submit button
        btnSubmit.setOnClickListener(v -> savePatient());

        return view;
    }

    private void setupGenderDropdown() {
        String[] genders = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                R.id.dropdown_item,
                genders
        );
        actvGender.setAdapter(adapter);
    }

    private void setupBloodTypeDropdown() {
        String[] bloodTypes = new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Unknown"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                R.id.dropdown_item,
                bloodTypes
        );
        actvBloodType.setAdapter(adapter);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etDob.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void savePatient() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Create patient object
        Patient patient = new Patient(
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etDob.getText().toString().trim(),
                actvGender.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                actvBloodType.getText().toString().trim(),
                etAllergies.getText().toString().trim(),
                etMedicalHistory.getText().toString().trim()
        );

        // Save patient to database using ViewModel
        long patientId = patientViewModel.insert(patient);

        if (patientId > 0) {
            Toast.makeText(requireContext(), "Patient added successfully", Toast.LENGTH_SHORT).show();
            // Navigate back to the patient list or details
            Navigation.findNavController(requireView()).navigateUp();
        } else {
            Toast.makeText(requireContext(), "Failed to add patient", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etLastName.getText())) {
            etLastName.setError("Last name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etDob.getText())) {
            etDob.setError("Date of birth is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(actvGender.getText())) {
            actvGender.setError("Gender is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Phone number is required");
            isValid = false;
        }

        return isValid;
    }
}