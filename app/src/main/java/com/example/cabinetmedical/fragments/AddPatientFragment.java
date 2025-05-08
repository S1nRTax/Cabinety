package com.example.cabinetmedical.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.models.Patient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddPatientFragment extends Fragment {

    private TextInputEditText etFirstName, etLastName, etDob, etPhone, etEmail, etAddress, etAllergies, etMedicalHistory;
    private AutoCompleteTextView actvGender, actvBloodType;
    private MaterialButton btnSubmit;

    public AddPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this method for non-view initialization
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Simply inflate the layout
        return inflater.inflate(R.layout.fragment_add_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        // Make sure dropdowns are properly focusable
        actvGender.setFocusableInTouchMode(true);
        actvGender.setFocusable(true);
        actvBloodType.setFocusableInTouchMode(true);
        actvBloodType.setFocusable(true);

        // Setup dropdowns
        setupGenderDropdown();
        setupBloodTypeDropdown();

        // Date picker for DOB
        etDob.setOnClickListener(v -> showDatePickerDialog());

        // Submit button
        btnSubmit.setOnClickListener(v -> savePatient());
    }

    @Override
    public void onDestroyView() {
        // Clean up any references to views to prevent leaks
        etFirstName = null;
        etLastName = null;
        etDob = null;
        etPhone = null;
        etEmail = null;
        etAddress = null;
        etAllergies = null;
        etMedicalHistory = null;
        actvGender = null;
        actvBloodType = null;
        btnSubmit = null;

        super.onDestroyView();
    }

    private void setupGenderDropdown() {
        String[] genders = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        actvGender.setAdapter(adapter);

        actvGender.setOnClickListener(v -> actvGender.showDropDown());
    }

    private void setupBloodTypeDropdown() {
        String[] bloodTypes = new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Unknown"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                bloodTypes
        );
        actvBloodType.setAdapter(adapter);
        actvBloodType.setOnClickListener(v -> actvBloodType.showDropDown());
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
        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            return;
        }

        Patient patient = new Patient(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etDob.getText().toString(),
                actvGender.getText().toString(),
                etPhone.getText().toString(),
                etEmail.getText().toString(),
                etAddress.getText().toString(),
                actvBloodType.getText().toString(),
                etAllergies.getText().toString(),
                etMedicalHistory.getText().toString()
        );

        // TODO: Save patient to database
        // Then navigate back or show success message
    }
}