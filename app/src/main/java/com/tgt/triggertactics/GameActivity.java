package com.tgt.triggertactics;

import android.content.Intent;
import android.net.Uri;
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

            btnWiki1.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://counterstrike.fandom.com/wiki/Category:Counter-Strike:_Global_Offensive_weapons"));
                startActivity(intent);
            });
            btnWiki2.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://counterstrike.fandom.com/wiki/Category:Counter-Strike:_Global_Offensive_Bomb_maps"));
                startActivity(intent);
            });
        } else if (game.equals("dota")) {
            textGameName.setText("Dota 2");
            btnWiki3.setVisibility(View.GONE);
            btnWiki1.setText("Heroes");
            btnWiki2.setText("Items");
            btnWiki1.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dota2.fandom.com/wiki/Heroes"));
                startActivity(intent);
            });
            btnWiki2.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dota2.fandom.com/wiki/Items"));
                startActivity(intent);
            });
        } else if (game.equals("valorant")) {
            textGameName.setText("VALORANT");
            btnWiki1.setText("Agents");
            btnWiki2.setText("Weapons");
            btnWiki3.setText("Maps");
            btnWiki1.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://valorant.fandom.com/wiki/Agents"));
                startActivity(intent);
            });
            btnWiki2.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://valorant.fandom.com/wiki/Weapons"));
                startActivity(intent);
            });
            btnWiki3.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://valorant.fandom.com/wiki/Maps"));
                startActivity(intent);
            });
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