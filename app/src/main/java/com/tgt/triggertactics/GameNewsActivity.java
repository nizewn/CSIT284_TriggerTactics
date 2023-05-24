package com.tgt.triggertactics;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameNewsActivity extends AppCompatActivity {
    String game;

    ImageButton btnBack;

    TextView textGameName,
            newsTitle1,
            newsTitle2,
            newsTitle3,
            newsDesc1,
            newsDesc2,
            newsDesc3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_news);

        btnBack = findViewById(R.id.btnBack13);
        btnBack.setOnClickListener(v -> finish());

        textGameName = findViewById(R.id.textGameName2);
        newsTitle1 = findViewById(R.id.newsTitle1);
        newsTitle2 = findViewById(R.id.newsTitle2);
        newsTitle3 = findViewById(R.id.newsTitle3);
        newsDesc1 = findViewById(R.id.newsDesc1);
        newsDesc2 = findViewById(R.id.newsDesc2);
        newsDesc3 = findViewById(R.id.newsDesc3);

        game = getIntent().getStringExtra("game");

        if (game.equals("csgo")) {
            textGameName.setText("CS:GO");
            newsTitle1.setText("Source 2 Update");
            newsDesc1.setText("CS:GO has been ported to Source 2 and is now available to play in beta. The update also includes a new operation, new maps, and new weapons.");

            newsTitle2.setText("New Operation");
            newsDesc2.setText("Operation Riptide is now available. It includes 6 new maps, 2 new agents, and 2 new weapon collections.");

            newsTitle3.setText("New Map: Ancient");
            newsDesc3.setText("Ancient is now available in the competitive map pool. It is a large map with a lot of open space and long sightlines.");
        } else if (game.equals("valorant")) {
            textGameName.setText("VALORANT");
            newsTitle1.setText("New Agent: KAY/O");
            newsDesc1.setText("KAY/O is a robot agent that can suppress enemies and disable their abilities. He is now available to play.");

            newsTitle2.setText("New Map: Fracture");
            newsDesc2.setText("Fracture is now available in the competitive map pool. It is a large map with a lot of open space and long sightlines.");

            newsTitle3.setText("New Weapon: Prime 2.0");
            newsDesc3.setText("The Prime 2.0 collection is now available. It includes skins for the Vandal, Spectre, Frenzy, and Melee.");
        } else if (game.equals("dota")) {
            textGameName.setText("Dota 2");
            newsTitle1.setText("The International 10");
            newsDesc1.setText("The International 10 is now live. The prize pool is currently $40 million, the largest in esports history.");

            newsTitle2.setText("New Hero: Dawnbreaker");
            newsDesc2.setText("Dawnbreaker is a melee strength hero that can heal allies and stun enemies. She is now available to play.");
            
            newsTitle3.setText("New Item: Helm of the Dominator");
            newsDesc3.setText("Helm of the Dominator has been reworked. It now grants bonus health and damage, and can be upgraded into a Satanic.");
        }
    }
}