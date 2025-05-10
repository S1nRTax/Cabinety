package com.example.cabinetmedical.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.adapters.PatientAdapter;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.PatientViewModel;


public class PatientListFragment extends Fragment implements PatientAdapter.OnPatientClickListener {

    private PatientViewModel patientViewModel;
    private PatientAdapter patientAdapter;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use a simpler approach by getting NavController from the current Fragment's parent
        // This is safer as it doesn't depend on specific IDs that might not exist
        try {
            Fragment navHostFragment = getParentFragmentManager().getPrimaryNavigationFragment();
            if (navHostFragment instanceof NavHostFragment) {
                navController = NavHostFragment.findNavController(navHostFragment);
            } else {
                // If the above doesn't work, try a direct approach
                navController = NavHostFragment.findNavController(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // As a last resort, if all else fails, use the view approach
            try {
                navController = Navigation.findNavController(view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.patientRecyclerView);
        patientAdapter = new PatientAdapter(this);
        recyclerView.setAdapter(patientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                LinearLayoutManager.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Setup add patient button
        view.findViewById(R.id.fabAddPatient).setOnClickListener(v -> {
            if (navController != null) {
                // Navigate to add patient fragment
                navController.navigate(R.id.addPatient);
            }
        });

        // Observe patient data
        patientViewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
            if (patients != null) {
                patientAdapter.setPatients(patients);
            }
        });
    }

    @Override
    public void onPatientClick(Patient patient) {
        try {
            // Safely navigate with null checks
            if (getView() != null) {
                NavController navController = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putLong("patientId", patient.getId());
                navController.navigate(R.id.action_to_patient_detail, bundle);
            } else {
                Log.e("PatientListFragment", "View is null, cannot navigate");
            }
        } catch (Exception e) {
            Log.e("PatientListFragment", "Navigation error", e);
            // Fallback to old FragmentManager if needed
            Fragment detailFragment = new PatientDetailFragment();
            Bundle args = new Bundle();
            args.putLong("patientId", patient.getId());
            detailFragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}