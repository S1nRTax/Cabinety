package com.example.cabinetmedical.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.AppointmentViewModel;
import com.example.cabinetmedical.viewmodels.PatientViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private AppointmentViewModel appointmentViewModel;
    private PatientViewModel patientViewModel;
    private TextInputEditText etDate, etTime, etPatientName, etPurpose;
    private Spinner spinnerStatus;
    private Button btnSchedule;
    private Date selectedDate;
    private long selectedPatientId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        // Initialize views
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etPatientName = view.findViewById(R.id.etPatientName);
        etPurpose = view.findViewById(R.id.etPurpose);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        btnSchedule = view.findViewById(R.id.btnSchedule);

        // Setup status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.appointment_statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Set up date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Set up time picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Set up patient search
        etPatientName.setOnClickListener(v -> showPatientSearchDialog());

        // Set up schedule button
        btnSchedule.setOnClickListener(v -> scheduleAppointment());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(selectedYear, selectedMonth, selectedDay);
                    selectedDate = cal.getTime();
                    etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    if (selectedDate == null) {
                        selectedDate = Calendar.getInstance().getTime();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(selectedDate);
                    cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                    cal.set(Calendar.MINUTE, selectedMinute);
                    selectedDate = cal.getTime();
                    etTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showPatientSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Patient");

        // Create a search input
        final EditText input = new EditText(requireContext());
        input.setHint("Search by last name");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            String lastName = input.getText().toString().trim();
            if (!lastName.isEmpty()) {
                searchPatient(lastName);
            } else {
                Toast.makeText(requireContext(), "Please enter a last name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void searchPatient(String lastName) {
        patientViewModel.searchPatients(lastName).observe(getViewLifecycleOwner(), patients -> {
            if (patients != null && !patients.isEmpty()) {
                showPatientSelectionDialog(patients);
            } else {
                Toast.makeText(requireContext(), "No patients found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPatientSelectionDialog(List<Patient> patients) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Patient");

        // Create list of patient names
        String[] patientNames = new String[patients.size()];
        for (int i = 0; i < patients.size(); i++) {
            patientNames[i] = patients.get(i).getFirstName() + " " + patients.get(i).getLastName();
        }

        builder.setItems(patientNames, (dialog, which) -> {
            Patient selectedPatient = patients.get(which);
            selectedPatientId = selectedPatient.getId();
            etPatientName.setText(selectedPatient.getFirstName() + " " + selectedPatient.getLastName());
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void scheduleAppointment() {
        if (selectedPatientId == -1) {
            Toast.makeText(requireContext(), "Please select a patient", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String purpose = etPurpose.getText().toString().trim();
        if (purpose.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter appointment purpose", Toast.LENGTH_SHORT).show();
            return;
        }

        String status = spinnerStatus.getSelectedItem().toString();

        // Check if time slot is available
        appointmentViewModel.countAppointmentsAtTime(selectedDate).observe(getViewLifecycleOwner(), count -> {
            if (count > 0) {
                Toast.makeText(requireContext(), "This time slot is already taken", Toast.LENGTH_SHORT).show();
            } else {
                Appointment appointment = new Appointment(selectedPatientId, selectedDate, purpose, status);
                long id = appointmentViewModel.insert(appointment);
                Toast.makeText(requireContext(), "Appointment scheduled successfully", Toast.LENGTH_SHORT).show();
                // TODO: Schedule notification for patient
                requireActivity().onBackPressed();
            }
        });
    }
}