package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomepageActivity extends AppCompatActivity {
    ImageButton btnProfileImage;
    Button btnOpenForums;

    TextView homeHelloText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mAuth = FirebaseAuth.getInstance();

        btnProfileImage = findViewById(R.id.btnProfileImage);
        btnProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnOpenForums = findViewById(R.id.btnOpenForums);
        btnOpenForums.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ForumsActivity.class);
            startActivity(intent);
        });

        homeHelloText = findViewById(R.id.homeHelloText);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            homeHelloText.setText("Hello, " + user.getDisplayName());
        }
    }
}