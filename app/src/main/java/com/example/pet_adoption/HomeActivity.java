package com.example.pet_adoption;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvPets;
    private PetAdapter adapter;
    private List<PetModel> allPetsList;
    private List<PetModel> filteredPetsList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ChipGroup chipGroup;

    private String currentQuery = "";
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hide ActionBar for a clean UI
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        rvPets = findViewById(R.id.rvPets);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchView);
        chipGroup = findViewById(R.id.chipGroup);

        // Setup RecyclerView
        rvPets.setLayoutManager(new LinearLayoutManager(this));
        allPetsList = new ArrayList<>();
        filteredPetsList = new ArrayList<>();
        adapter = new PetAdapter(filteredPetsList);
        rvPets.setAdapter(adapter);

        // Setup Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                filterPets();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText;
                filterPets();
                return true;
            }
        });

        // Setup Filters
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = "All";
            } else {
                int id = checkedIds.get(0);
                if (id == R.id.chipAll) {
                    currentCategory = "All";
                } else if (id == R.id.chipDogs) {
                    currentCategory = "Dog";
                } else if (id == R.id.chipCats) {
                    currentCategory = "Cat";
                }
            }
            filterPets();
        });

        fetchPetsFromFirestore();
    }

    private void fetchPetsFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);

        // Using addSnapshotListener for real-time updates as requested for dynamic updates
        db.collection("pets")
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);
                    if (error != null) {
                        Log.e("FirestoreError", error.getMessage());
                        Toast.makeText(this, "Error loading pets", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        allPetsList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            PetModel pet = document.toObject(PetModel.class);
                            allPetsList.add(pet);
                        }
                        filterPets();
                    }
                });
    }

    private void filterPets() {
        filteredPetsList.clear();
        for (PetModel pet : allPetsList) {
            boolean matchesSearch = pet.getBreed().toLowerCase().contains(currentQuery.toLowerCase());
            boolean matchesCategory = currentCategory.equals("All") || 
                    (pet.getPetType() != null && pet.getPetType().equalsIgnoreCase(currentCategory));

            if (matchesSearch && matchesCategory) {
                filteredPetsList.add(pet);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
