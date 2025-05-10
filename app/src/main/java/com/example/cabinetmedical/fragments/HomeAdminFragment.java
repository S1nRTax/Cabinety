package com.example.cabinetmedical.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.activities.AdminActivity;

public class HomeAdminFragment extends Fragment {

    private CardView cardAddPatient;
    private CardView cardEditPatient;
    private CardView cardRemovePatient;
    private CardView cardSchedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        cardAddPatient = view.findViewById(R.id.cardAddPatient);
        cardEditPatient = view.findViewById(R.id.cardEditPatient);
        cardRemovePatient = view.findViewById(R.id.cardRemovePatient);
        cardSchedule = view.findViewById(R.id.cardSchedule);

        // Set click listener for Add Patient card
        cardAddPatient.setOnClickListener(v -> {
            navigateToAddPatient();
        });

        // Set click listeners for other cards
        cardEditPatient.setOnClickListener(v -> {
            // Handle edit patient action
        });

        cardRemovePatient.setOnClickListener(v -> {
            navigateToRemovePatient();
        });

        cardSchedule.setOnClickListener(v -> {
            // Handle schedule action
        });
    }

    // Method to navigate to AddPatientFragment
    private void navigateToAddPatient() {
        if (getActivity() instanceof AdminActivity) {
            ((AdminActivity) getActivity()).navigateToAddPatient();
        }
    }

    private void navigateToRemovePatient() {
        if (getActivity() instanceof AdminActivity) {
            ((AdminActivity) getActivity()).navigateToRemovePatient();
        }
    }
}