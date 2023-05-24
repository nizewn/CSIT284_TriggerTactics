package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomepageActivity extends AppCompatActivity {
    ImageButton btnProfileImage, btnSearchActivity;
    Button btnOpenForums, btnOpenMediaHub, btnOpenScrims, btnOpenChatList;

    ImageButton btnOpenCsgo, btnOpenDota, btnOpenValorant;

    TextView homeHelloText;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    String teamName;
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
            if (teamName == null || teamName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "You must be in a team to access this feature", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), ScrimsActivity.class);
                intent.putExtra("teamName", teamName);
                startActivity(intent);
            }
        });

        btnOpenChatList = findViewById(R.id.btnOpenChatList);
        btnOpenChatList.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
            startActivity(intent);
        });

        btnOpenCsgo = findViewById(R.id.btnOpenCsgo);
        btnOpenDota = findViewById(R.id.btnOpenDota);
        btnOpenValorant = findViewById(R.id.btnOpenValorant);

        btnOpenCsgo.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("game", "csgo");
            startActivity(intent);
        });

        btnOpenDota.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("game", "dota");
            startActivity(intent);
        });

        btnOpenValorant.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("game", "valorant");
            startActivity(intent);
        });

        homeHelloText = findViewById(R.id.homeHelloText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();

        homeHelloText.setText("Hello, " + user.getDisplayName());
        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(btnProfileImage);
        }
        database.getReference().child("users").child(user.getUid()).child("team").get().addOnSuccessListener(task -> {
            if (task.getValue() != null && !task.getValue().toString().isEmpty()) {
                teamName = task.getValue().toString();
            }
        });
    }
}