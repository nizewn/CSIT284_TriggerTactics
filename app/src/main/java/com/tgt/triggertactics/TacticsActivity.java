package com.tgt.triggertactics;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TacticsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String game;

    String map;

    TextView textGameName;
    ImageButton btnBack;

    Spinner spinnerSelectMap;
    ImageView imageMap;
    EditText editTextTactics;
    Button btnSaveTactics;

    LinearLayout selectMapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tactics);

        String uid = mAuth.getCurrentUser().getUid();

        game = getIntent().getStringExtra("game");

        btnBack = findViewById(R.id.btnBack14);
        btnBack.setOnClickListener(v -> finish());

        textGameName = findViewById(R.id.textGameName3);
        spinnerSelectMap = findViewById(R.id.spinnerSelectMap);
        imageMap = findViewById(R.id.imageMap);
        editTextTactics = findViewById(R.id.editTextTactics);
        btnSaveTactics = findViewById(R.id.btnSaveTactics);

        ArrayList<String> maps = new ArrayList<>();
        if (game.equals("csgo")) {
            textGameName.setText("CS:GO");

            maps.add("Dust II");
            maps.add("Mirage");
            maps.add("Inferno");
        } else if (game.equals("dota")) {
            textGameName.setText("Dota 2");
            map = "dota";
            selectMapContainer = findViewById(R.id.linearLayout31);
            selectMapContainer.setVisibility(View.GONE);
            imageMap.setImageResource(R.drawable.dota_map);
            database.getReference("tactics/" + uid + "/" + map).get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    editTextTactics.setText(dataSnapshot.getValue(String.class));
                }
            });
        } else if (game.equals("valorant")) {
            textGameName.setText("VALORANT");

            maps.add("Ascent");
            maps.add("Bind");
            maps.add("Haven");
        }

        if (!game.equals("dota")) {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maps);
            spinnerSelectMap.setAdapter(spinnerAdapter);
            spinnerSelectMap.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    map = maps.get(position);
                    database.getReference("tactics/" + uid + "/" + map).get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()) {
                            editTextTactics.setText(dataSnapshot.getValue(String.class));
                        }
                    });
                    switch (map) {
                        case "Dust II":
                            imageMap.setImageResource(R.drawable.dust2);
                            break;
                        case "Mirage":
                            imageMap.setImageResource(R.drawable.mirage);
                            break;
                        case "Inferno":
                            imageMap.setImageResource(R.drawable.inferno);
                            break;
                        case "Ascent":
                            imageMap.setImageResource(R.drawable.ascent);
                            break;
                        case "Bind":
                            imageMap.setImageResource(R.drawable.bind);
                            break;
                        case "Haven":
                            imageMap.setImageResource(R.drawable.haven);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        btnSaveTactics.setOnClickListener(v -> {
            if (map == null || map.isEmpty()) return;

            String tactics = editTextTactics.getText().toString();
            if (!tactics.isEmpty()) {
                database.getReference("tactics/" + uid + "/" + map).setValue(tactics).addOnSuccessListener(l -> {
                    Toast.makeText(this, map + " tactics saved!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


}