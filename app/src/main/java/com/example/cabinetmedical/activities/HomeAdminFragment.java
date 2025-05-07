package com.example.cabinetmedical.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.cabinetmedical.R;

public class HomeAdminFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // Initialize your card views here
        CardView cardAddPatient = view.findViewById(R.id.cardAddPatient);
        cardAddPatient.setOnClickListener(v -> {
            // Handle add patient
        });

        return view;
    }
}
