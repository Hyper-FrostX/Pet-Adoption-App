package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class PetDetailActivity extends AppCompatActivity {

    private ImageView ivPetDetailImage;
    private TextView tvPetName, tvBreed, tvAge, tvGender, tvShelter, tvDescription;
    private MaterialButton btnAdoptNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        // Initialize Views
        ivPetDetailImage = findViewById(R.id.ivPetDetailImage);
        tvPetName = findViewById(R.id.tvPetDetailName);
        tvBreed = findViewById(R.id.tvPetDetailBreed);
        tvAge = findViewById(R.id.tvPetDetailAge);
        tvGender = findViewById(R.id.tvPetDetailGender);
        tvShelter = findViewById(R.id.tvPetDetailShelter);
        tvDescription = findViewById(R.id.tvPetDetailDescription);
        btnAdoptNow = findViewById(R.id.btnAdoptNow);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Receive Intent extras
        String name = getIntent().getStringExtra("petName");
        String breed = getIntent().getStringExtra("breed");
        String age = getIntent().getStringExtra("age");
        String gender = getIntent().getStringExtra("gender");
        String shelter = getIntent().getStringExtra("shelterName");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");

        // Display data in UI
        tvPetName.setText(name);
        tvBreed.setText(breed);
        tvAge.setText(age);
        tvGender.setText(gender);
        tvShelter.setText(shelter);
        tvDescription.setText(description);

        // Load image with Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_paw)
                .error(R.drawable.ic_paw)
                .into(ivPetDetailImage);

        btnAdoptNow.setOnClickListener(v -> {
            Intent intent = new Intent(PetDetailActivity.this, AdoptionRequestActivity.class);
            intent.putExtra("petId", getIntent().getStringExtra("petId"));
            intent.putExtra("petName", name);
            intent.putExtra("shelterId", getIntent().getStringExtra("shelterId"));
            intent.putExtra("shelterName", shelter);
            startActivity(intent);
        });
    }
}
