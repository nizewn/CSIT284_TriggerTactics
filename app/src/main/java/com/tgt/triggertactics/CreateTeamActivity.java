package com.tgt.triggertactics;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTeamActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ImageButton btnBack;

    CheckBox createTeamCsgo, createTeamDota, createTeamValorant;

    EditText createTeamName;
    Button btnCreateTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        btnBack = findViewById(R.id.btnBack6);
        btnBack.setOnClickListener(view -> finish());

        createTeamCsgo = findViewById(R.id.createTeamCsgo);
        createTeamDota = findViewById(R.id.createTeamDota);
        createTeamValorant = findViewById(R.id.createTeamValorant);
        createTeamName = findViewById(R.id.createTeamName);
        btnCreateTeam = findViewById(R.id.btnCreateTeam);

        btnCreateTeam.setOnClickListener(v -> {
            btnCreateTeam.setEnabled(false);
            String teamName = createTeamName.getText().toString();
            if (teamName.isEmpty()) {
                createTeamName.setError("Please enter a team name");
                createTeamName.requestFocus();
                btnCreateTeam.setEnabled(true);
                return;
            }
            if (!createTeamCsgo.isChecked() && !createTeamDota.isChecked() && !createTeamValorant.isChecked()) {
                createTeamCsgo.setError("Please select at least one game");
                createTeamCsgo.requestFocus();
                btnCreateTeam.setEnabled(true);
                return;
            }
            database.getReference().child("teams").get().addOnSuccessListener(snapshot -> {
                if (snapshot.hasChild(teamName)) {
                    createTeamName.setError("Team name already exists");
                    createTeamName.requestFocus();
                    btnCreateTeam.setEnabled(true);
                    return;
                }
                String uid = mAuth.getCurrentUser().getUid();
                database.getReference("teams/" + teamName + "/csgo").setValue(createTeamCsgo.isChecked());
                database.getReference("teams/" + teamName + "/dota").setValue(createTeamDota.isChecked());
                database.getReference("teams/" + teamName + "/valorant").setValue(createTeamValorant.isChecked());
                database.getReference("teams/" + teamName + "/leader").setValue(uid);
                database.getReference("teams/" + teamName + "/members/" + uid).setValue(true);
                database.getReference("users/" + uid + "/team").setValue(teamName).addOnSuccessListener(s -> {
                    finish();
                });
            });
        });
    }
}