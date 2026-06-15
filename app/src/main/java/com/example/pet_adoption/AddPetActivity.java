package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPetActivity extends AppCompatActivity {

    private TextInputEditText etPetName, etBreed, etAge, etImageUrl, etDescription;
    private Spinner spinnerPetType, spinnerGender;
    private MaterialButton btnAddPet;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String shelterName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }
        currentUserId = mAuth.getCurrentUser().getUid();

        // Initialize Views
        etPetName = findViewById(R.id.etPetName);
        etBreed = findViewById(R.id.etBreed);
        etAge = findViewById(R.id.etAge);
        etImageUrl = findViewById(R.id.etImageUrl);
        etDescription = findViewById(R.id.etDescription);
        spinnerPetType = findViewById(R.id.spinnerPetType);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnAddPet = findViewById(R.id.btnAddPet);
        progressBar = findViewById(R.id.progressBar);

        fetchShelterInfo();

        btnAddPet.setOnClickListener(v -> validateAndAddPet());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Pet");
        }
    }

    private void fetchShelterInfo() {
        db.collection("shelters").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        shelterName = documentSnapshot.getString("shelterName");
                    }
                });
    }

    private void validateAndAddPet() {
        String name = etPetName.getText().toString().trim();
        String breed = etBreed.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String petType = spinnerPetType.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            etPetName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(breed)) {
            etBreed.setError("Breed is required");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Age is required");
            return;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            etImageUrl.setError("Image URL is required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required");
            return;
        }

        savePetToFirestore(name, breed, age, imageUrl, description, petType, gender);
    }

    private void savePetToFirestore(String name, String breed, String age, String imageUrl, String description, String petType, String gender) {
        progressBar.setVisibility(View.VISIBLE);
        btnAddPet.setEnabled(false);

        String petId = UUID.randomUUID().toString();

        Map<String, Object> pet = new HashMap<>();
        pet.put("petId", petId);
        pet.put("petName", name);
        pet.put("breed", breed);
        pet.put("age", age);
        pet.put("imageUrl", imageUrl);
        pet.put("description", description);
        pet.put("petType", petType);
        pet.put("gender", gender);
        pet.put("shelterId", currentUserId);
        pet.put("shelterName", shelterName);

        db.collection("pets").document(petId)
                .set(pet)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddPetActivity.this, "Pet Added Successfully", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(AddPetActivity.this, ShelterDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnAddPet.setEnabled(true);
                    Toast.makeText(AddPetActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
