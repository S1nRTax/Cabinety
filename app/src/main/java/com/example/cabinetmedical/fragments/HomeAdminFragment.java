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

        cardAddPatient = view.findViewById(R.id.cardAddPatient);
        cardEditPatient = view.findViewById(R.id.cardEditPatient);
        cardRemovePatient = view.findViewById(R.id.cardRemovePatient);
        cardSchedule = view.findViewById(R.id.cardSchedule);

        cardAddPatient.setOnClickListener(v -> {
            navigateToAddPatient();
        });

        cardEditPatient.setOnClickListener(v -> {
            navigateToEditPatient();
        });

        cardRemovePatient.setOnClickListener(v -> {
            navigateToRemovePatient();
        });

        cardSchedule.setOnClickListener(v -> {
            // Handle schedule action
        });
    }

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

    private void navigateToEditPatient() {
        if (getActivity() instanceof AdminActivity) {
            ((AdminActivity) getActivity()).navigateToEditPatient();
        }
    }
}