package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView textGameName;

    Button btnGameNews, btnGameTactics, btnWiki1, btnWiki2, btnWiki3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnBack = findViewById(R.id.btnBack12);
        btnBack.setOnClickListener(v -> finish());

        btnWiki1 = findViewById(R.id.btnWiki1);
        btnWiki2 = findViewById(R.id.btnWiki2);
        btnWiki3 = findViewById(R.id.btnWiki3);

        textGameName = findViewById(R.id.textGameName);
        String game = getIntent().getStringExtra("game");
        if (game.equals("csgo")) {
            textGameName.setText("CS:GO");
            btnWiki3.setVisibility(View.GONE);

            btnWiki1.setText("Weapons");
            btnWiki2.setText("Maps");
            // TODO: open external wiki/fandom
        } else if (game.equals("dota")) {
            textGameName.setText("Dota 2");
            btnWiki3.setVisibility(View.GONE);
            btnWiki1.setText("Heroes");
            btnWiki2.setText("Items");
            // TODO: open external wiki/fandom
        } else if (game.equals("valorant")) {
            textGameName.setText("VALORANT");
            btnWiki1.setText("Agents");
            btnWiki2.setText("Weapons");
            btnWiki3.setText("Maps");
            // TODO: open external wiki/fandom
        }

        btnGameNews = findViewById(R.id.btnGameNews);
        btnGameTactics = findViewById(R.id.btnGameTactics);

        btnGameNews.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GameNewsActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });
        btnGameTactics.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TacticsActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });
    }
}