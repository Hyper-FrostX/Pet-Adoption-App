package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShelterRegisterActivity extends AppCompatActivity {

    private TextInputLayout tilShelterName, tilEmail, tilPhone, tilLocation, tilPassword, tilConfirmPassword;
    private TextInputEditText etShelterName, etEmail, etPhone, etLocation, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        tilShelterName = findViewById(R.id.tilShelterName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilLocation = findViewById(R.id.tilLocation);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etShelterName = findViewById(R.id.etShelterName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void validateAndRegister() {
        String name = etShelterName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Reset errors
        tilShelterName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        if (TextUtils.isEmpty(name)) {
            tilShelterName.setError(getString(R.string.error_shelter_name_required));
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            return;
        }

        if (password.length() < 6) {
            tilPassword.setError(getString(R.string.error_password_too_short));
            return;
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_do_not_match));
            return;
        }

        registerShelter(name, email, phone, location, password);
    }

    private void registerShelter(String name, String email, String phone, String location, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        saveShelterInfo(userId, name, email, phone, location);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        Toast.makeText(ShelterRegisterActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveShelterInfo(String userId, String name, String email, String phone, String location) {
        Map<String, Object> shelter = new HashMap<>();
        shelter.put("shelterId", userId);
        shelter.put("shelterName", name);
        shelter.put("email", email);
        shelter.put("phoneNumber", phone);
        shelter.put("location", location);
        shelter.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());

        db.collection("shelters").document(userId)
                .set(shelter)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ShelterRegisterActivity.this, getString(R.string.shelter_registration_success), Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(ShelterRegisterActivity.this, ShelterDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    Toast.makeText(ShelterRegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
