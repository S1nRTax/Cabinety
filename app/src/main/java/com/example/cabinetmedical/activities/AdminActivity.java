package com.example.cabinetmedical.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.fragments.AppointmentsFragment;
import com.example.cabinetmedical.fragments.HomeAdminFragment;
import com.example.cabinetmedical.fragments.PatientListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private CardView cardAddPatient;
    private CardView cardEditPatient;
    private CardView cardRemovePatient;
    private CardView cardSchedule;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeAdminFragment());
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (itemId == R.id.navigationHome && !(currentFragment instanceof HomeAdminFragment)) {
                loadFragment(new HomeAdminFragment());
                return true;
            } else if (itemId == R.id.navigationPatientList && !(currentFragment instanceof PatientListFragment)) {
                loadFragment(new PatientListFragment());
                return true;
            } else if (itemId == R.id.navigationAppointments && !(currentFragment instanceof AppointmentsFragment)) {
                loadFragment(new AppointmentsFragment());
                return true;
            } else if (itemId == R.id.navigationLogout) {
                logout();
                return true;
            }
            return false;
        });
    }

    public void loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // to prevent loading the same fragment two times.
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }

        int enterAnim, exitAnim, popEnterAnim, popExitAnim;

        if (shouldAnimateForward(currentFragment, fragment)) {
            //forward
            enterAnim = R.anim.slide_in_right;
            exitAnim = R.anim.slide_out_left;
            popEnterAnim = R.anim.slide_in_left;
            popExitAnim = R.anim.slide_out_right;
        } else {
            //backward
            enterAnim = R.anim.slide_in_left;
            exitAnim = R.anim.slide_out_right;
            popEnterAnim = R.anim.slide_in_right;
            popExitAnim = R.anim.slide_out_left;
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean shouldAnimateForward(Fragment current, Fragment next) {
        if (current instanceof HomeAdminFragment && next instanceof PatientListFragment) {
            return true; // Home → Patients
        }
        if (current instanceof HomeAdminFragment && next instanceof AppointmentsFragment) {
            return true; // Home → Appointments
        }
        if (current instanceof PatientListFragment && next instanceof AppointmentsFragment) {
            return true; // Patients → Appointments
        }
        return false; // backward.
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
