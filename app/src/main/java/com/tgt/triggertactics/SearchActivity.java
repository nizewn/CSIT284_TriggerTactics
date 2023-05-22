package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<DataSnapshot> userSnapshots, teamSnapshots;

    ImageButton btnBack, btnSearchStart;
    EditText editTextSearch;
    LinearLayout linearSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.editTextSearch);

        btnBack = findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(view -> finish());

        btnSearchStart = findViewById(R.id.btnSearchStart);
        linearSearchList = findViewById(R.id.linearSearchList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        getAllUsers();
        getAllTeams();

        btnSearchStart.setOnClickListener(view -> {
            if (userSnapshots == null) return;
            String search = editTextSearch.getText().toString();
            if (search.equals("")) {
                editTextSearch.setError("Please enter a search term");
                return;
            }
            linearSearchList.removeAllViews();
            for (DataSnapshot userSnapshot : userSnapshots) {
                String userDisplayName = userSnapshot.child("displayname").getValue().toString();
                if (userDisplayName.toLowerCase().contains(search.toLowerCase())) {
                    View searchItem = inflater.inflate(R.layout.layout_item, linearSearchList, false);
                    Button btnDisplayName = searchItem.findViewById(R.id.itemBtnName);
                    ImageButton imageBtnUser = searchItem.findViewById(R.id.itemImage);
                    TextView textUserGames = searchItem.findViewById(R.id.itemCaption);

                    btnDisplayName.setText(userDisplayName);
                    Picasso.get().load(userSnapshot.child("imageurl").getValue().toString()).into(imageBtnUser);

                    ArrayList<String> games = new ArrayList<>();

                    if (userSnapshot.child("games").child("csgo").getValue() != null && userSnapshot.child("games").child("csgo").getValue().toString().equals("true"))
                        games.add("CS:GO");
                    if (userSnapshot.child("games").child("dota").getValue() != null && userSnapshot.child("games").child("dota").getValue().toString().equals("true"))
                        games.add("Dota 2");
                    if (userSnapshot.child("games").child("valorant").getValue() != null && userSnapshot.child("games").child("valorant").getValue().toString().equals("true"))
                        games.add("Valorant");

                    if (userSnapshot.child("games").child("other").getValue() != null && !userSnapshot.child("games").child("other").getValue().toString().equals("")) {
                        games.add(userSnapshot.child("games").child("other").getValue().toString());
                    }

                    textUserGames.setText(String.join(", ", games));

                    String profileId = userSnapshot.getKey();
                    if (profileId != mAuth.getCurrentUser().getUid()) {
                        btnDisplayName.setOnClickListener(v -> {
                            Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                            intent.putExtra("profileId", profileId);
                            startActivity(intent);
                        });
                        imageBtnUser.setOnClickListener(v -> {

                            Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                            intent.putExtra("profileId", profileId);
                            startActivity(intent);
                        });
                    }


                    linearSearchList.addView(searchItem);
                }
            }
            for (DataSnapshot teamSnapshot : teamSnapshots) {
                String teamName = teamSnapshot.getKey();
                if (teamName.toLowerCase().contains(search.toLowerCase())) {
                    View searchItem = inflater.inflate(R.layout.layout_item, linearSearchList, false);
                    Button btnDisplayName = searchItem.findViewById(R.id.itemBtnName);
                    ImageButton imageBtnUser = searchItem.findViewById(R.id.itemImage);
                    imageBtnUser.setVisibility(View.GONE);
                    TextView txtTeamCaption = searchItem.findViewById(R.id.itemCaption);

                    btnDisplayName.setText("(Team) " + teamName);

                    ArrayList<String> games = new ArrayList<>();

                    if (teamSnapshot.child("csgo").getValue() != null && teamSnapshot.child("csgo").getValue(Boolean.class))
                        games.add("CS:GO");
                    if (teamSnapshot.child("dota").getValue() != null && teamSnapshot.child("dota").getValue(Boolean.class))
                        games.add("Dota 2");
                    if (teamSnapshot.child("valorant").getValue() != null && teamSnapshot.child("valorant").getValue(Boolean.class))
                        games.add("Valorant");

                    txtTeamCaption.setText(String.join(", ", games));

                    btnDisplayName.setOnClickListener(v -> {
                        Intent intent = new Intent(getApplicationContext(), TeamProfileActivity.class);
                        intent.putExtra("teamName", teamName);
                        startActivity(intent);
                    });

                    linearSearchList.addView(searchItem);
                }
            }
        });
    }

    private void getAllUsers() {
        userSnapshots = new ArrayList<>();
        database.getReference("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    userSnapshots.add(userSnapshot);
                }
            }
        });
    }

    private void getAllTeams() {
        teamSnapshots = new ArrayList<>();
        database.getReference("teams").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot teamSnapshot : task.getResult().getChildren()) {
                    teamSnapshots.add(teamSnapshot);
                }
            }
        });
    }
}