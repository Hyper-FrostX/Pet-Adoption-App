package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private TextView btnLoginRedirect;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide ActionBar for a clean UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views using existing IDs
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        btnLoginRedirect = findViewById(R.id.btnLoginRedirect);

        // Ensure the first field is focused to enable typing
        etName.requestFocus();

        // Register Button Click Listener
        btnRegister.setOnClickListener(v -> registerUser());

        // Login Redirect Click Listener
        btnLoginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation Logic
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Show ProgressBar and disable button
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        // Firebase: Create User with Email and Password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Hide ProgressBar and enable button
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);

                    if (task.isSuccessful()) {
                        // Registration successful
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        
                        // Redirect to LoginActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        // Clear the activity stack so user can't go back to RegisterActivity
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); 
                    } else {
                        // Registration failed, show error message
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}