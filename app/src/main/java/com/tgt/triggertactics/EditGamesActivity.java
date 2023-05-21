package com.tgt.triggertactics;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class EditGamesActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btnCancelGames, btnSaveGames;
    CheckBox checkboxCsgo, checkboxDota, checkboxValorant;
    EditText editTextOtherGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_games);

        checkboxCsgo = findViewById(R.id.checkboxCsgo);
        checkboxDota = findViewById(R.id.checkboxDota);
        checkboxValorant = findViewById(R.id.checkboxValorant);
        editTextOtherGames = findViewById(R.id.editTextOtherGames);

        btnCancelGames = findViewById(R.id.btnCancelGames);
        btnSaveGames = findViewById(R.id.btnSaveGames);

        btnCancelGames.setOnClickListener(view -> finish());
        btnSaveGames.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null)
                return;
            if (checkboxCsgo.isChecked())
                database.getReference("users/" + user.getUid() + "/games/csgo").setValue(true);
            else
                database.getReference("users/" + user.getUid() + "/games/csgo").setValue(false);
            if (checkboxDota.isChecked())
                database.getReference("users/" + user.getUid() + "/games/dota").setValue(true);
            else
                database.getReference("users/" + user.getUid() + "/games/dota").setValue(false);
            if (checkboxValorant.isChecked())
                database.getReference("users/" + user.getUid() + "/games/valorant").setValue(true);
            else
                database.getReference("users/" + user.getUid() + "/games/valorant").setValue(false);

            database.getReference("users/" + user.getUid() + "/games/other").setValue(editTextOtherGames.getText().toString());
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    protected void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            return;
        database.getReference("users/" + user.getUid() + "/games").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Boolean csgo = Boolean.parseBoolean(task.getResult().child("csgo").getValue().toString());
                Boolean dota = Boolean.parseBoolean(task.getResult().child("dota").getValue().toString());
                Boolean valorant = Boolean.parseBoolean(task.getResult().child("valorant").getValue().toString());
                String other = task.getResult().child("other").getValue().toString();

                checkboxCsgo.setChecked(csgo);
                checkboxDota.setChecked(dota);
                checkboxValorant.setChecked(valorant);
                editTextOtherGames.setText(other);
            }
        });
    }
}