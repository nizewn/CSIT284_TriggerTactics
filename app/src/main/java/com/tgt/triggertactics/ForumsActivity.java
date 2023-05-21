package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ForumsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnBack;

    Button btnForumValorant, btnForumCsgo, btnForumDota,
            btnForumIntroductions, btnForumEvents, btnForumFeedback, btnForumGames, btnForumTech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        btnForumValorant = findViewById(R.id.btnForumValorant);
        btnForumCsgo = findViewById(R.id.btnForumCsgo);
        btnForumDota = findViewById(R.id.btnForumDota);
        btnForumIntroductions = findViewById(R.id.btnForumIntroductions);
        btnForumEvents = findViewById(R.id.btnForumEvents);
        btnForumFeedback = findViewById(R.id.btnForumFeedback);
        btnForumGames = findViewById(R.id.btnForumGames);
        btnForumTech = findViewById(R.id.btnForumTech);

        btnForumValorant.setOnClickListener(this);
        btnForumCsgo.setOnClickListener(this);
        btnForumDota.setOnClickListener(this);
        btnForumIntroductions.setOnClickListener(this);
        btnForumEvents.setOnClickListener(this);
        btnForumFeedback.setOnClickListener(this);
        btnForumGames.setOnClickListener(this);
        btnForumTech.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ThreadListActivity.class);
        if (view.getId() == R.id.btnForumValorant) {
            intent.putExtra("forum", "valorant");
        } else if (view.getId() == R.id.btnForumCsgo) {
            intent.putExtra("forum", "csgo");
        } else if (view.getId() == R.id.btnForumDota) {
            intent.putExtra("forum", "dota");
        } else if (view.getId() == R.id.btnForumIntroductions) {
            intent.putExtra("forum", "introductions");
        } else if (view.getId() == R.id.btnForumEvents) {
            intent.putExtra("forum", "events");
        } else if (view.getId() == R.id.btnForumFeedback) {
            intent.putExtra("forum", "feedback");
        } else if (view.getId() == R.id.btnForumGames) {
            intent.putExtra("forum", "games");
        } else if (view.getId() == R.id.btnForumTech) {
            intent.putExtra("forum", "tech");
        } else {
            return;
        }
        startActivity(intent);
    }
}