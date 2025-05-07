package com.example.cabinetmedical.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.cabinetmedical.R;
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

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationHome) {
                loadFragment(new HomeAdminFragment());
                return true;
            } else if (itemId == R.id.navigationPatientList) {
                loadFragment(new PatientListFragment());
                return true;
            } else if (itemId == R.id.navigationAppointments) {
                loadFragment(new AppointmentsFragment());
                return true;
            } else if (itemId == R.id.navigationLogout) {
                logout();
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // to prevent loading the same fragment two times.
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
