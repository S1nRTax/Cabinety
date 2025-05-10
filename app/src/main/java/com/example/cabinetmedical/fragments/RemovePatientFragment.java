package com.example.cabinetmedical.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

public class RemovePatientFragment extends Fragment {

    private PatientViewModel patientViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
        showRemovePatientDialog();
    }

    private void showRemovePatientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Remove Patient");
        builder.setMessage("Enter the patient's last name to remove:");

        final EditText input = new EditText(requireContext());
        builder.setView(input);

        builder.setPositiveButton("Remove", (dialog, which) -> {
            String lastName = input.getText().toString().trim();
            if (!lastName.isEmpty()) {
                removePatientByLastName(lastName);
            } else {
                Toast.makeText(requireContext(), "Please enter a last name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            // Go back to previous fragment
            requireActivity().onBackPressed();
        });

        builder.setOnCancelListener(dialog -> {
            // Go back to previous fragment if dialog is canceled
            requireActivity().onBackPressed();
        });

        builder.show();
    }

    private void removePatientByLastName(String lastName) {
        patientViewModel.getPatientByLastName(lastName).observe(getViewLifecycleOwner(), patient -> {
            if (patient != null) {
                // Patient found, confirm removal
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Removal")
                        .setMessage("Are you sure you want to remove " + patient.getFirstName() + " " + patient.getLastName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            patientViewModel.delete(patient);
                            Toast.makeText(requireContext(), "Patient removed successfully", Toast.LENGTH_SHORT).show();
                            requireActivity().onBackPressed();
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                // Patient not found
                Toast.makeText(requireContext(), "Patient with last name '" + lastName + "' not found", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });
    }
}