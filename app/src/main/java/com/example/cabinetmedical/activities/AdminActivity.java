package com.example.cabinetmedical.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.fragments.AddPatientFragment;
import com.example.cabinetmedical.fragments.AppointmentsFragment;
import com.example.cabinetmedical.fragments.EditPatientFragment;
import com.example.cabinetmedical.fragments.HomeAdminFragment;
import com.example.cabinetmedical.fragments.PatientListFragment;
import com.example.cabinetmedical.fragments.RemovePatientFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT";
    private static final String PATIENT_LIST_FRAGMENT_TAG = "PATIENT_LIST_FRAGMENT";
    private static final String APPOINTMENTS_FRAGMENT_TAG = "APPOINTMENTS_FRAGMENT";
    private static final String ADD_PATIENT_FRAGMENT_TAG = "ADD_PATIENT_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeAdminFragment(), false, HOME_FRAGMENT_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // If we're on a fragment other than Home, go back to Home
        if (fragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG) == null &&
                fragmentManager.getBackStackEntryCount() > 0) {

            // Clear the entire back stack
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Load the home fragment
            loadFragment(new HomeAdminFragment(), false, HOME_FRAGMENT_TAG);

            // Update bottom navigation selection
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);
        } else {
            super.onBackPressed();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Check which navigation item was selected
            if (itemId == R.id.navigationHome) {
                loadFragment(new HomeAdminFragment(), false, HOME_FRAGMENT_TAG);
                return true;
            } else if (itemId == R.id.navigationPatientList) {
                loadFragment(new PatientListFragment(), false, PATIENT_LIST_FRAGMENT_TAG);
                return true;
            } else if (itemId == R.id.navigationAppointments) {
                loadFragment(new AppointmentsFragment(), false, APPOINTMENTS_FRAGMENT_TAG);
                return true;
            } else if (itemId == R.id.navigationLogout) {
                logout();
                return true;
            }
            return false;
        });
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Set custom animations
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );

        // Remove any existing fragments first
        clearAllFragments(fragmentManager);

        // Add the new fragment with a tag
        transaction.replace(R.id.fragment_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }

        // Commit the transaction with allowStateLoss to avoid IllegalStateException
        transaction.commitAllowingStateLoss();
    }

    /**
     * Method to clear all fragments from the manager to avoid overlapping
     */
    private void clearAllFragments(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        Fragment homeFragment = fragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG);
        Fragment patientListFragment = fragmentManager.findFragmentByTag(PATIENT_LIST_FRAGMENT_TAG);
        Fragment appointmentsFragment = fragmentManager.findFragmentByTag(APPOINTMENTS_FRAGMENT_TAG);
        Fragment addPatientFragment = fragmentManager.findFragmentByTag(ADD_PATIENT_FRAGMENT_TAG);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (homeFragment != null) {
            transaction.remove(homeFragment);
        }
        if (patientListFragment != null) {
            transaction.remove(patientListFragment);
        }
        if (appointmentsFragment != null) {
            transaction.remove(appointmentsFragment);
        }
        if (addPatientFragment != null) {
            transaction.remove(addPatientFragment);
        }

        // Apply removals if needed
        if (!transaction.isEmpty()) {
            transaction.commitNowAllowingStateLoss();
        }
    }

    /**
     * Method to navigate to AddPatientFragment
     */
    public void navigateToAddPatient() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        clearAllFragments(fragmentManager);

        // Then load the AddPatientFragment with animation
        loadFragment(new AddPatientFragment(), true, ADD_PATIENT_FRAGMENT_TAG);
    }

    public void navigateToRemovePatient() {
        RemovePatientFragment fragment = new RemovePatientFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToEditPatient() {
        EditPatientFragment fragment = new EditPatientFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToListOfPatient(){
        PatientListFragment fragment = new PatientListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    preferences.edit().clear().apply();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .create()
                .show();
    }
}