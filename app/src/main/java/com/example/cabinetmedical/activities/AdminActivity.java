package com.example.cabinetmedical.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

        // init
        cardAddPatient = findViewById(R.id.cardAddPatient);
        cardEditPatient = findViewById(R.id.cardEditPatient);
        cardRemovePatient = findViewById(R.id.cardRemovePatient);
        cardSchedule = findViewById(R.id.cardSchedule);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//
//        cardAddPatient.setOnClickListener(v -> {
//            startActivity(new Intent(this, AddPatientActivity.class));
//        });
//
//        cardEditPatient.setOnClickListener(v -> {
//            startActivity(new Intent(this, EditPatientActivity.class));
//        });
//
//        cardRemovePatient.setOnClickListener(v -> {
//            startActivity(new Intent(this, RemovePatientActivity.class));
//        });
//
//        cardSchedule.setOnClickListener(v -> {
//            startActivity(new Intent(this, ScheduleActivity.class));
//        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigationHome) {
                startActivity(new Intent(this, AdminActivity.class));
                return true;
            } else if (itemId == R.id.navigationPatientList) {
                startActivity(new Intent(this, PatientListActivity.class));
                return true;
            } else if (itemId == R.id.navigationAppointments) {
                startActivity(new Intent(this, AppointmentsActivity.class));
                return true;
            } else if (itemId == R.id.navigationLogout) {
                // Clear login preferences
                SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                preferences.edit()
                        .clear()
                        .apply();

                // Navigate to login and clear stack
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

    }
}
