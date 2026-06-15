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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide ActionBar for a clean UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Automatically sign out any previously logged in user
        mAuth.signOut();

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        // Login Button Click Listener
        btnLogin.setOnClickListener(v -> loginUser());

        // Register Text Click Listener
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Validation Logic
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

        // 2. Show ProgressBar and disable button
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // 3. Firebase: Sign In with Email and Password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Hide ProgressBar and enable button
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {
                        // Login successful
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        
                        // Redirect to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        // Clear the activity stack so user can't go back to LoginActivity
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); 
                    } else {
                        // Login failed, show error message
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}