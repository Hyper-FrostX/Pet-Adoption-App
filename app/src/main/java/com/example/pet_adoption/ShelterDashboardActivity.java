package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ShelterDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalPets, tvPendingRequests, tvApprovedRequests;
    private MaterialButton btnAddPet, btnManagePets, btnViewRequests, btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, ShelterLoginActivity.class));
            finish();
            return;
        }
        
        currentUserId = mAuth.getCurrentUser().getUid();

        // Initialize Views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalPets = findViewById(R.id.tvTotalPets);
        tvPendingRequests = findViewById(R.id.tvPendingRequests);
        tvApprovedRequests = findViewById(R.id.tvApprovedRequests);
        
        btnAddPet = findViewById(R.id.btnAddPet);
        btnManagePets = findViewById(R.id.btnManagePets);
        btnViewRequests = findViewById(R.id.btnViewRequests);
        btnLogout = findViewById(R.id.btnLogout);

        setupClickListeners();
        loadDashboardData();
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ShelterDashboardActivity.this, ShelterLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnAddPet.setOnClickListener(v -> {
            Intent intent = new Intent(ShelterDashboardActivity.this, AddPetActivity.class);
            startActivity(intent);
        });

        btnManagePets.setOnClickListener(v -> {
            Intent intent = new Intent(ShelterDashboardActivity.this, ManagePetsActivity.class);
            startActivity(intent);
        });

        btnViewRequests.setOnClickListener(v -> {
            Intent intent = new Intent(ShelterDashboardActivity.this, ViewRequestsActivity.class);
            startActivity(intent);
        });
    }

    private void loadDashboardData() {
        // Fetch Shelter Name
        db.collection("shelters").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("shelterName");
                        tvWelcome.setText("Welcome, " + name);
                    }
                });

        // Fetch Total Pets Count
        db.collection("pets")
                .whereEqualTo("shelterId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tvTotalPets.setText(String.valueOf(queryDocumentSnapshots.size()));
                });

        // Fetch Pending Requests Count
        db.collection("adoptionRequests")
                .whereEqualTo("shelterId", currentUserId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tvPendingRequests.setText(String.valueOf(queryDocumentSnapshots.size()));
                });

        // Fetch Approved Requests Count
        db.collection("adoptionRequests")
                .whereEqualTo("shelterId", currentUserId)
                .whereEqualTo("status", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tvApprovedRequests.setText(String.valueOf(queryDocumentSnapshots.size()));
                });
    }
}
