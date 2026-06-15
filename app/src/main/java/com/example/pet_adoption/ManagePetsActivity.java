package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagePetsActivity extends AppCompatActivity implements PetManagementAdapter.OnPetActionListener {

    private RecyclerView rvManagePets;
    private PetManagementAdapter adapter;
    private List<PetModel> petList;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pets);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }
        currentUserId = mAuth.getCurrentUser().getUid();

        rvManagePets = findViewById(R.id.rvManagePets);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        petList = new ArrayList<>();
        adapter = new PetManagementAdapter(petList, this);

        rvManagePets.setLayoutManager(new LinearLayoutManager(this));
        rvManagePets.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Pets");
        }

        fetchPets();
    }

    private void fetchPets() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("pets")
                .whereEqualTo("shelterId", currentUserId)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    petList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            PetModel pet = doc.toObject(PetModel.class);
                            petList.add(pet);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    tvEmptyState.setVisibility(petList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    @Override
    public void onEdit(PetModel pet) {
        Intent intent = new Intent(this, EditPetActivity.class);
        intent.putExtra("pet", pet);
        startActivity(intent);
    }

    @Override
    public void onDelete(PetModel pet) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Pet")
                .setMessage("Are you sure you want to delete " + pet.getPetName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deletePet(pet);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePet(PetModel pet) {
        db.collection("pets").document(pet.getPetId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Pet deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
