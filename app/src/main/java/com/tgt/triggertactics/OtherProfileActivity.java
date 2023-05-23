package com.tgt.triggertactics;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OtherProfileActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    int reputation = 0;
    ImageView otherProfileImage;
    ImageButton btnBack;
    TextView otherProfileName, otherProfileReputation, otherProfileGames;
    Button btnOtherProfileMessage, btnPlusReputation, btnMinusReputation;

    String chatId, profileId, currentUserId;

    String profileImage, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        btnBack = findViewById(R.id.btnBack5);
        btnBack.setOnClickListener(v -> finish());

        otherProfileImage = findViewById(R.id.otherProfileImage);
        otherProfileName = findViewById(R.id.otherProfileName);
        otherProfileReputation = findViewById(R.id.otherProfileReputation);
        otherProfileGames = findViewById(R.id.otherProfileGames);

        btnOtherProfileMessage = findViewById(R.id.btnOtherProfileMessage);
        btnPlusReputation = findViewById(R.id.btnPlusReputation);
        btnMinusReputation = findViewById(R.id.btnMinusReputation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        profileId = getIntent().getStringExtra("profileId");
        currentUserId = mAuth.getCurrentUser().getUid();

        int blueColorInt = Color.parseColor("#01C0FA");
        int greyColorInt = Color.parseColor("#5E5E5E");
        database.getReference("reputation").child(currentUserId).get().addOnSuccessListener(snapshot -> {

            if (snapshot.hasChild(profileId)) {
                if (snapshot.child(profileId).getValue().toString().equals("true")) {
                    ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(blueColorInt));
                    ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
                } else {
                    ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                    ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(blueColorInt));
                }
            } else {
                ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
            }
        });

        btnPlusReputation.setOnClickListener(v -> {
            btnPlusReputation.setEnabled(false);
            database.getReference("reputation").child(currentUserId).get().addOnSuccessListener(snapshot -> {
                Map<String, Object> updates = new HashMap<>();
                if (snapshot.hasChild(profileId)) {
                    if (snapshot.child(profileId).getValue().toString().equals("true")) {
                        database.getReference("reputation").child(currentUserId).child(profileId).removeValue();
                        updates.put("users/" + profileId + "/reputation", ServerValue.increment(-1));
                        reputation--;
                        ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                        ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
                    } else {
                        database.getReference("reputation").child(currentUserId).child(profileId).setValue("true");
                        updates.put("users/" + profileId + "/reputation", ServerValue.increment(2));
                        reputation += 2;
                        ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(blueColorInt));
                        ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
                    }
                } else {
                    database.getReference("reputation").child(currentUserId).child(profileId).setValue("true");
                    updates.put("users/" + profileId + "/reputation", ServerValue.increment(1));
                    reputation++;
                    ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(blueColorInt));
                    ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
                }
                otherProfileReputation.setText("Reputation: " + reputation);
                database.getReference().updateChildren(updates);
                btnPlusReputation.setEnabled(true);
            });
        });

        btnMinusReputation.setOnClickListener(v -> {
            btnMinusReputation.setEnabled(false);
            database.getReference("reputation").child(currentUserId).get().addOnSuccessListener(snapshot -> {
                Map<String, Object> updates = new HashMap<>();
                if (snapshot.hasChild(profileId)) {
                    if (snapshot.child(profileId).getValue().toString().equals("true")) {
                        database.getReference("reputation").child(currentUserId).child(profileId).setValue("false");
                        updates.put("users/" + profileId + "/reputation", ServerValue.increment(-2));
                        reputation -= 2;
                        ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                        ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(blueColorInt));
                    } else {
                        database.getReference("reputation").child(currentUserId).child(profileId).removeValue();
                        updates.put("users/" + profileId + "/reputation", ServerValue.increment(1));
                        reputation++;
                        ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                        ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(greyColorInt));
                    }
                } else {
                    database.getReference("reputation").child(currentUserId).child(profileId).setValue("false");
                    updates.put("users/" + profileId + "/reputation", ServerValue.increment(-1));
                    reputation--;
                    ViewCompat.setBackgroundTintList(btnPlusReputation, ColorStateList.valueOf(greyColorInt));
                    ViewCompat.setBackgroundTintList(btnMinusReputation, ColorStateList.valueOf(blueColorInt));
                }
                otherProfileReputation.setText("Reputation: " + reputation);
                database.getReference().updateChildren(updates);
                btnMinusReputation.setEnabled(true);
            });
        });

        database.getReference("users").child(profileId).get().addOnSuccessListener(snapshot -> {

            profileName = snapshot.child("displayname").getValue().toString();
            reputation = snapshot.child("reputation").getValue(Integer.class);
            profileImage = snapshot.child("imageurl").getValue().toString();

            otherProfileName.setText(profileName);
            otherProfileReputation.setText("Reputation: " + reputation);
            Picasso.get().load(profileImage).into(otherProfileImage);

            ArrayList<String> games = new ArrayList<>();

            if (snapshot.child("games").child("csgo").getValue() != null && snapshot.child("games").child("csgo").getValue().toString().equals("true"))
                games.add("CS:GO");
            if (snapshot.child("games").child("dota").getValue() != null && snapshot.child("games").child("dota").getValue().toString().equals("true"))
                games.add("Dota 2");
            if (snapshot.child("games").child("valorant").getValue() != null && snapshot.child("games").child("valorant").getValue().toString().equals("true"))
                games.add("Valorant");

            if (snapshot.child("games").child("other").getValue() != null && !snapshot.child("games").child("other").getValue().toString().equals("")) {
                games.add(snapshot.child("games").child("other").getValue().toString());
            }
            otherProfileGames.setText(String.join(", ", games));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        database.getReference("chat").get().addOnSuccessListener(l -> {
            boolean found = false;
            for (DataSnapshot chatSn : l.getChildren()) {
                if (chatSn.child("participants/" + profileId).exists() && chatSn.child("participants/" + currentUserId).exists()) {
                    chatId = chatSn.getKey();
                    found = true;
                    break;
                }
            }
            if (!found) {
                chatId = "none";
            }
        });

        btnOtherProfileMessage.setOnClickListener(l -> {
            if (chatId == null) return;
            if (!chatId.isEmpty()) {
                btnOtherProfileMessage.setEnabled(false);
            }
            if (chatId.equals("none")) {
                chatId = database.getReference("chat").push().getKey();
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("chat/" + chatId + "/participants/" + currentUserId, true);
                updates.put("chat/" + chatId + "/participants/" + profileId, true);
                database.getReference().updateChildren(updates).addOnSuccessListener(sc -> {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("chatId", chatId);
                    intent.putExtra("otherImage", profileImage);
                    intent.putExtra("otherName", profileName);
                    startActivity(intent);
                    btnOtherProfileMessage.setEnabled(true);
                });
            } else if (!chatId.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("otherImage", profileImage);
                intent.putExtra("otherName", profileName);
                startActivity(intent);
                btnOtherProfileMessage.setEnabled(true);
            }
        });
    }
}