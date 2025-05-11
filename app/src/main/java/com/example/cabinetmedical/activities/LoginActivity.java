package com.example.cabinetmedical.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.viewmodels.PatientViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.example.cabinetmedical.utils.check;



public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhone;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private TextInputLayout inputLayoutPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            if (prefs.getBoolean("isAdmin", false)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        // init
        editTextPhone = findViewById(R.id.register_fullname);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        progressBar = findViewById(R.id.progress_bar);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // User already logged in, redirect to MainActivity
            startMainActivity();
            return;
        }

        buttonLogin.setOnClickListener(v -> attemptLogin());
        buttonRegister.setOnClickListener(v -> openRegistration());
    }

    private void attemptLogin() {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);

        String phoneNumber = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (phoneNumber.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }

        if(phoneNumber.startsWith("0")){
            editTextPhone.setError("Phone number should not start with 0");
            editTextPhone.requestFocus();
            return;
        }

        if(!check.phoneNumber(phoneNumber, "212")){
            editTextPhone.setError("Invalid phone number format");
            editTextPhone.requestFocus();
            return;
        }

        if (!check.Password(password)) {
            inputLayoutPassword.setError("Invalid password");
            inputLayoutPassword.setEndIconVisible(false);
            editTextPassword.requestFocus();
            return;
        } else {
            inputLayoutPassword.setError(null);
            inputLayoutPassword.setEndIconVisible(true);
        }

        progressBar.setVisibility(View.VISIBLE);
        authenticateUser(phoneNumber, password);
    }

    private void authenticateUser(String phoneNumber, String password) {
        progressBar.setVisibility(View.VISIBLE);

        // Admin check
        String adminUser = getString(R.string.admin_user);
        String adminPass = getString(R.string.admin_password);

        if (phoneNumber.equals(adminUser) && password.equals(adminPass)) {
            // Admin login
            saveLoginState(true, phoneNumber, true, -1);
            startAdminActivity();
            return;
        }

        // Patient login
        PatientViewModel patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        patientViewModel.loginPatient(phoneNumber, password).observe(this, patient -> {
            progressBar.setVisibility(View.GONE);

            if (patient != null) {
                saveLoginState(true, phoneNumber, false, patient.getId());
                startMainActivity();
            } else {
                Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginState(boolean isLoggedIn, String username, boolean isAdmin, long patientId) {
        SharedPreferences.Editor editor = getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("username", username);
        editor.putBoolean("isAdmin", isAdmin);
        editor.putLong("patientId", patientId);
        editor.apply();  // Make sure to use apply() instead of commit()
    }

    private void startAdminActivity() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
