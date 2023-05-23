package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TeamProfileActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageButton btnBack;
    Button btnTeamProfileAction;
    EditText editTeamName;
    CheckBox checkTeamCsgo, checkTeamDota, checkTeamValorant;
    LinearLayout linearMemberList;
    String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);

        btnBack = findViewById(R.id.btnBack7);
        btnBack.setOnClickListener(view -> finish());

        btnTeamProfileAction = findViewById(R.id.btnTeamProfileAction);
        editTeamName = findViewById(R.id.editTeamName);
        checkTeamCsgo = findViewById(R.id.checkTeamCsgo);
        checkTeamDota = findViewById(R.id.checkTeamDota);
        checkTeamValorant = findViewById(R.id.checkTeamValorant);
        linearMemberList = findViewById(R.id.linearMemberList);

        teamName = getIntent().getStringExtra("teamName");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMembers();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uid = mAuth.getCurrentUser().getUid();

        editTeamName.setText(teamName);

        database.getReference("teams/" + teamName).get().addOnSuccessListener(snapshot -> {
            checkTeamCsgo.setChecked(snapshot.child("csgo").getValue(Boolean.class));
            checkTeamDota.setChecked(snapshot.child("dota").getValue(Boolean.class));
            checkTeamValorant.setChecked(snapshot.child("valorant").getValue(Boolean.class));
            if (snapshot.child("leader").getValue().equals(uid)) {
                btnTeamProfileAction.setText("Save Team Profile");
                btnTeamProfileAction.setOnClickListener(v -> {
                    database.getReference("teams/" + teamName).child("csgo").setValue(checkTeamCsgo.isChecked());
                    database.getReference("teams/" + teamName).child("dota").setValue(checkTeamDota.isChecked());
                    database.getReference("teams/" + teamName).child("valorant").setValue(checkTeamValorant.isChecked());
                });
            } else {
                btnTeamProfileAction.setOnClickListener(v2 -> {
                    btnTeamProfileAction.setEnabled(false);
                    database.getReference("users/" + uid + "/team").get().addOnSuccessListener(snapshot1 -> {

                        if (snapshot1.exists() && snapshot1.getValue().equals(teamName)) {
                            database.getReference("users/" + uid + "/team").removeValue();
                            database.getReference("teams/" + teamName + "/members/" + uid).removeValue().addOnSuccessListener(sn -> {
                                btnTeamProfileAction.setText("Join Team");
                                updateMembers();
                                btnTeamProfileAction.setEnabled(true);
                            });
                        } else {
                            database.getReference("users/" + uid + "/team").setValue(teamName);
                            database.getReference("teams/" + teamName + "/members/" + uid).setValue(true).addOnSuccessListener(sn -> {
                                btnTeamProfileAction.setText("Leave Team");
                                updateMembers();
                                btnTeamProfileAction.setEnabled(true);
                            });
                        }
                    });
                });
                database.getReference("users/" + uid + "/team").get().addOnSuccessListener(snapshot1 -> {
                    if (snapshot1.exists() && snapshot1.getValue().equals(teamName)) {
                        btnTeamProfileAction.setText("Leave Team");
                    } else {
                        btnTeamProfileAction.setText("Join Team");
                    }
                });
            }
        });
    }

    private void updateMembers() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        String currentUid = mAuth.getCurrentUser().getUid();
        database.getReference("teams/" + teamName + "/members").get().addOnSuccessListener(snapshot -> {
            linearMemberList.removeAllViews();
            for (DataSnapshot child : snapshot.getChildren()) {
                String uid = child.getKey();
                View view = inflater.inflate(R.layout.layout_item, linearMemberList, false);
                Button btnMemberName = view.findViewById(R.id.itemBtnName);
                ImageButton btnMemberImage = view.findViewById(R.id.itemImage);
                TextView txtCaption = view.findViewById(R.id.itemCaption);

                database.getReference("teams/" + teamName + "/leader").get().addOnSuccessListener(snapshot1 -> {
                    if (snapshot1.getValue().equals(uid)) {
                        txtCaption.setText("Leader");
                    } else {
                        txtCaption.setVisibility(View.GONE);
                    }
                });

                database.getReference("users/" + uid).get().addOnSuccessListener(snapshot1 -> {
                    String displayName = snapshot1.child("displayname").getValue(String.class);
                    String imageUrl = snapshot1.child("imageurl").getValue(String.class);
                    btnMemberName.setText(displayName);
                    Picasso.get().load(imageUrl).into(btnMemberImage);

                    if (uid != currentUid) {
                        btnMemberName.setOnClickListener(v -> {
                            Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                            intent.putExtra("profileId", uid);
                            startActivity(intent);
                        });
                        btnMemberImage.setOnClickListener(v -> {
                            Intent intent = new Intent(getApplicationContext(), OtherProfileActivity.class);
                            intent.putExtra("profileId", uid);
                            startActivity(intent);
                        });
                    }

                    linearMemberList.addView(view);
                });
            }
        });
    }
}