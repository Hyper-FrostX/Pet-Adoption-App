package com.example.pet_adoption;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide ActionBar for a clean, modern look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}