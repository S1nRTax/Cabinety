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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.google.android.material.textfield.TextInputLayout;

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
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.edit_text_phone);
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

        if(phoneNumber.startsWith("06")){
            Log.d("error", phoneNumber);
        }


        if (phoneNumber.isEmpty()) {
            editTextPhone.setError("Username required");
            editTextPhone.requestFocus();
            return;
        }

        if(!phoneNumber.startsWith("06")){
            editTextPhone.setError("Enter a valid phone number - starts with 06");
            editTextPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inputLayoutPassword.setError("Password required");
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

    private void authenticateUser(String username, String password) {
        // For demonstration purposes only, using hardcoded credentials
        // TODO: Replace with proper authentication system
        if (username.equals("admin") && password.equals("admin123")) {
            // Save login status
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("username", username);
            editor.apply();

            // Navigate to main screen
            startMainActivity();
        } else {
            // Authentication failed
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openRegistration() {
        // Open registration screen or show registration dialog
    }
}
