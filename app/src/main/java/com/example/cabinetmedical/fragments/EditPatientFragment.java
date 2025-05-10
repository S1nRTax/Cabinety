package com.example.cabinetmedical.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

import java.util.Calendar;

public class EditPatientFragment extends Fragment {

    private PatientViewModel patientViewModel;
    private CardView cardEditForm;
    private EditText etSearchLastName, etFirstName, etLastName, etDob;
    private Button btnSearchPatient, btnUpdatePatient;
    private Patient currentPatient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        // Initialize views
        cardEditForm = view.findViewById(R.id.cardEditForm);
        etSearchLastName = view.findViewById(R.id.etSearchLastName);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etDob = view.findViewById(R.id.etDob);
        btnSearchPatient = view.findViewById(R.id.btnSearchPatient);
        btnUpdatePatient = view.findViewById(R.id.btnUpdatePatient);

        // Set up search button
        btnSearchPatient.setOnClickListener(v -> searchPatient());

        // Set up update button
        btnUpdatePatient.setOnClickListener(v -> updatePatient());


    }

    private void searchPatient() {
        String lastName = etSearchLastName.getText().toString().trim();
        if (!lastName.isEmpty()) {
            patientViewModel.getPatientByLastName(lastName).observe(getViewLifecycleOwner(), patient -> {
                if (patient != null) {
                    currentPatient = patient;
                    populateForm(patient);
                    cardEditForm.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(requireContext(), "Patient not found", Toast.LENGTH_SHORT).show();
                    cardEditForm.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(requireContext(), "Please enter a last name", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateForm(Patient patient) {
        etFirstName.setText(patient.getFirstName());
        etLastName.setText(patient.getLastName());
        etDob.setText(patient.getDateOfBirth());
        // Populate other fields similarly
    }

    private void updatePatient() {
        if (currentPatient == null) return;

        // Get updated values from form
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update patient object
        currentPatient.setFirstName(firstName);
        currentPatient.setLastName(lastName);
        currentPatient.setDateOfBirth(dob);
        // Update other fields similarly

        // Save to database
        patientViewModel.update(currentPatient);
        Toast.makeText(requireContext(), "Patient updated successfully", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        etDob.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }
}