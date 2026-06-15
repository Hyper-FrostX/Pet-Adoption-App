package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ShelterLoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> validateAndLogin());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ShelterLoginActivity.this, ShelterRegisterActivity.class);
            startActivity(intent);
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Reset errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            return;
        }

        loginShelter(email, password);
    }

    private void loginShelter(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ShelterLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShelterLoginActivity.this, ShelterDashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        btnLogin.setEnabled(true);
                        Toast.makeText(ShelterLoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
