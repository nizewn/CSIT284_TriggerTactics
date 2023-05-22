package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class HomepageActivity extends AppCompatActivity {
    ImageButton btnProfileImage, btnSearchActivity;
    Button btnOpenForums, btnOpenMediaHub, btnOpenScrims, btnOpenChatList;

    ImageButton btnOpenCsgo, btnOpenDota, btnOpenValorant;

    TextView homeHelloText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mAuth = FirebaseAuth.getInstance();

        btnProfileImage = findViewById(R.id.btnProfileImage);
        btnProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        btnSearchActivity = findViewById(R.id.btnSearchActivity);
        btnSearchActivity.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });

        btnOpenForums = findViewById(R.id.btnOpenForums);
        btnOpenForums.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ForumsActivity.class);
            startActivity(intent);
        });

        btnOpenMediaHub = findViewById(R.id.btnOpenMediaHub);
        btnOpenMediaHub.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MediaHubActivity.class);
            startActivity(intent);
        });

        btnOpenScrims = findViewById(R.id.btnOpenScrims);
        btnOpenScrims.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ScrimsActivity.class);
            startActivity(intent);
        });

        btnOpenChatList = findViewById(R.id.btnOpenChatList);
        btnOpenChatList.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
            startActivity(intent);
        });

        btnOpenCsgo = findViewById(R.id.btnOpenCsgo);
        btnOpenDota = findViewById(R.id.btnOpenDota);
        btnOpenValorant = findViewById(R.id.btnOpenValorant);
        // TODO: Add onClickListeners for these buttons

        homeHelloText = findViewById(R.id.homeHelloText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            homeHelloText.setText("Hello, " + user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl()).into(btnProfileImage);
            }
        }
    }
}