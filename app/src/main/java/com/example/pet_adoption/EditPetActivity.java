package com.example.pet_adoption;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPetActivity extends AppCompatActivity {

    private TextInputEditText etPetName, etBreed, etAge, etImageUrl, etDescription;
    private Spinner spinnerPetType, spinnerGender;
    private MaterialButton btnUpdatePet;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private PetModel pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        db = FirebaseFirestore.getInstance();

        // Get the pet object from intent
        pet = (PetModel) getIntent().getSerializableExtra("pet");
        if (pet == null) {
            Toast.makeText(this, "Error: Pet data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Views
        etPetName = findViewById(R.id.etPetName);
        etBreed = findViewById(R.id.etBreed);
        etAge = findViewById(R.id.etAge);
        etImageUrl = findViewById(R.id.etImageUrl);
        etDescription = findViewById(R.id.etDescription);
        spinnerPetType = findViewById(R.id.spinnerPetType);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnUpdatePet = findViewById(R.id.btnUpdatePet);
        progressBar = findViewById(R.id.progressBar);

        // Populate fields with existing data
        populateFields();

        btnUpdatePet.setOnClickListener(v -> validateAndUpdatePet());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Pet Details");
        }
    }

    private void populateFields() {
        etPetName.setText(pet.getPetName());
        etBreed.setText(pet.getBreed());
        etAge.setText(pet.getAge());
        etImageUrl.setText(pet.getImageUrl());
        etDescription.setText(pet.getDescription());

        // Set Spinner values
        setSpinnerValue(spinnerPetType, R.array.pet_types, pet.getPetType());
        setSpinnerValue(spinnerGender, R.array.genders, pet.getGender());
    }

    private void setSpinnerValue(Spinner spinner, int arrayResId, String value) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    private void validateAndUpdatePet() {
        String name = etPetName.getText().toString().trim();
        String breed = etBreed.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String petType = spinnerPetType.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            etPetName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(breed)) {
            etBreed.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            etImageUrl.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Required");
            return;
        }

        updatePetInFirestore(name, breed, age, imageUrl, description, petType, gender);
    }

    private void updatePetInFirestore(String name, String breed, String age, String imageUrl, String description, String petType, String gender) {
        progressBar.setVisibility(View.VISIBLE);
        btnUpdatePet.setEnabled(false);

        Map<String, Object> updates = new HashMap<>();
        updates.put("petName", name);
        updates.put("breed", breed);
        updates.put("age", age);
        updates.put("imageUrl", imageUrl);
        updates.put("description", description);
        updates.put("petType", petType);
        updates.put("gender", gender);

        db.collection("pets").document(pet.getPetId())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Pet updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdatePet.setEnabled(true);
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
