package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    Button btnLogout, btnEditProfileActivity, btnProfileTeam;
    TextView textViewProfileName, textViewProfileEmail;
    ImageView profileImageView;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnEditProfileActivity = findViewById(R.id.btnEditProfileActivity);
        btnEditProfileActivity.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        textViewProfileName = findViewById(R.id.textViewProfileName);
        textViewProfileEmail = findViewById(R.id.textViewProfileEmail);
        profileImageView = findViewById(R.id.profileImageView);

        btnProfileTeam = findViewById(R.id.btnProfileTeam);


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) return;
        textViewProfileName.setText(currentUser.getDisplayName());
        textViewProfileEmail.setText(currentUser.getEmail());
        if (currentUser.getPhotoUrl() != null)
            Picasso.get().load(currentUser.getPhotoUrl()).into(profileImageView);

        String currentUserId = mAuth.getCurrentUser().getUid();
        database.getReference("users/" + currentUserId + "/team").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                btnProfileTeam.setText("Team: " + snapshot.getValue().toString());
                btnProfileTeam.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), TeamProfileActivity.class);
                    intent.putExtra("teamName", snapshot.getValue().toString());
                    startActivity(intent);
                });
            } else {
                btnProfileTeam.setText("Create Team");
                btnProfileTeam.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), CreateTeamActivity.class);
                    startActivity(intent);
                });
            }
        });
    }
}