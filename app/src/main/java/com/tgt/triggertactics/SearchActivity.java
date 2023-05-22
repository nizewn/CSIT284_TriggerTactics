package com.tgt.triggertactics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");

    ArrayList<DataSnapshot> userSnapshots;

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

        btnSearchStart.setOnClickListener(view -> {
            System.out.println("CLICK: 1");
            if (userSnapshots == null) return;
            System.out.println("CLICK: 2");
            String search = editTextSearch.getText().toString();
            if (search.equals("")) {
                editTextSearch.setError("Please enter a search term");
                return;
            }
            System.out.println("CLICK: 3");
            linearSearchList.removeAllViews();
            for (DataSnapshot userSnapshot : userSnapshots) {
                System.out.println("looping");
                String userDisplayName = userSnapshot.child("displayname").getValue().toString();
                System.out.println("userDisplayName: " + userDisplayName);
                if (userDisplayName.toLowerCase().contains(search.toLowerCase())) {
                    System.out.println("true userDisplayName: " + userDisplayName);
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
}