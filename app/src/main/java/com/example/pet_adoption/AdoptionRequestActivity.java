package com.example.pet_adoption;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class AdoptionRequestActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etPhone, etEmail, etAddress, etReason;
    private MaterialButton btnSubmitRequest;
    private String petName, shelterName;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_request);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get Pet Data from Intent
        petName = getIntent().getStringExtra("petName");
        shelterName = getIntent().getStringExtra("shelterName");

        // Initialize Views
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etReason = findViewById(R.id.etReason);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvHeader = findViewById(R.id.tvHeader);

        if (petName != null) {
            tvHeader.setText(getString(R.string.adopting_pet, petName));
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btnSubmitRequest.setOnClickListener(v -> validateAndSubmit());
    }

    private void validateAndSubmit() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String reason = etReason.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError(getString(R.string.error_name_required));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.error_phone_required));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_email_required));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError(getString(R.string.error_address_required));
            return;
        }
        if (TextUtils.isEmpty(reason)) {
            etReason.setError(getString(R.string.error_reason_required));
            return;
        }

        // Successfully validated
        submitRequest(name, phone, email, address, reason);
    }

    private void submitRequest(String name, String phone, String email, String address, String reason) {
        String petId = getIntent().getStringExtra("petId");
        String shelterId = getIntent().getStringExtra("shelterId");

        Map<String, Object> request = new HashMap<>();
        request.put("petId", petId);
        request.put("petName", petName);
        request.put("shelterId", shelterId);
        request.put("shelterName", shelterName);
        request.put("applicantName", name);
        request.put("phoneNumber", phone);
        request.put("email", email);
        request.put("address", address);
        request.put("reason", reason);
        request.put("status", "pending");
        request.put("requestDate", new Date());

        db.collection("adoptionRequests")
                .add(request)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, R.string.adoption_request_success, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, getString(R.string.error_prefix, e.getMessage()), Toast.LENGTH_SHORT).show();
                });
    }
}
