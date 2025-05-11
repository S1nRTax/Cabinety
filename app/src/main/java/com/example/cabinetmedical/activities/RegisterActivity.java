package com.example.cabinetmedical.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.viewmodels.PatientViewModel;

public class RegisterActivity extends AppCompatActivity {

    private Button backToLoginButton;

    // In RegisterActivity.java, update the onCreate method:
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        EditText etFullName = findViewById(R.id.register_fullname);
        EditText etPhone = findViewById(R.id.register_phone);
        EditText etPassword = findViewById(R.id.edit_text_password);
        EditText etConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        Button btnRegister = findViewById(R.id.button_register);
        Button btnBackToLogin = findViewById(R.id.button_back_to_login);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        PatientViewModel patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validation
            if (fullName.isEmpty()) {
                etFullName.setError("Full name is required");
                return;
            }

            if (phone.isEmpty()) {
                etPhone.setError("Phone number is required");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords don't match");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Check if phone number already exists
            patientViewModel.getPatientByPhoneNumber(phone).observe(this, patient -> {
                if (patient != null) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Create new patient (simplified for demo)
                    String[] names = fullName.split(" ");
                    String firstName = names[0];
                    String lastName = names.length > 1 ? names[1] : "";

                    Patient newPatient = new Patient(
                            firstName, lastName, "", "", phone, "", "",
                            "", "", "", password
                    );

                    long patientId = patientViewModel.insert(newPatient);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });

        btnBackToLogin.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
