package com.example.pet_adoption;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class RoleSelectionActivity extends AppCompatActivity {

    private MaterialButton btnUserLogin, btnShelterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        btnUserLogin = findViewById(R.id.btnUserLogin);
        btnShelterLogin = findViewById(R.id.btnShelterLogin);

        btnUserLogin.setOnClickListener(v -> {
            startActivity(new Intent(RoleSelectionActivity.this, LoginActivity.class));
        });

        btnShelterLogin.setOnClickListener(v -> {
            startActivity(new Intent(RoleSelectionActivity.this, ShelterLoginActivity.class));
        });
    }
}
