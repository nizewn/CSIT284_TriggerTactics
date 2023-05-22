package com.tgt.triggertactics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MediaHubActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ImageButton btnBack;
    Button btnPostMedia;
    EditText editMediaUrl, editMediaCaption;
    LinearLayout linearMediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_hub);

        btnBack = findViewById(R.id.btnBack8);
        btnBack.setOnClickListener(v -> finish());

        btnPostMedia = findViewById(R.id.btnPostMedia);
        editMediaUrl = findViewById(R.id.editMediaUrl);
        editMediaCaption = findViewById(R.id.editMediaCaption);
        linearMediaList = findViewById(R.id.linearMediaList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnPostMedia.setOnClickListener(v -> {
            String url = editMediaUrl.getText().toString();
            String caption = editMediaCaption.getText().toString();
            String uid = mAuth.getCurrentUser().getUid();

            String newKey = database.getReference("mediahub").push().getKey();
            HashMap<String, Object> values = new HashMap<>();
            values.put("url", url);
            values.put("caption", caption);
            values.put("authorId", uid);

            database.getReference("mediahub").child(newKey).updateChildren(values);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        database.getReference("mediahub").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearMediaList.removeAllViews();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String url = child.child("url").getValue().toString();
                    String caption = child.child("caption").getValue().toString();
                    String authorId = child.child("authorId").getValue().toString();

                    View view = inflater.inflate(R.layout.layout_item_media, linearMediaList, false);
                    TextView textMediaAuthor = view.findViewById(R.id.textMediaAuthor);
                    ImageView imageMedia = view.findViewById(R.id.imageMedia);
                    TextView textMediaCaption = view.findViewById(R.id.textMediaCaption);
                    TextView textMediaLink = view.findViewById(R.id.textMediaLink);

                    textMediaCaption.setText(caption);
                    textMediaLink.setText(url);

                    textMediaLink.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    });

                    database.getReference("users/" + authorId).get().addOnSuccessListener(userSnapshot -> {
                        String authorName = userSnapshot.child("displayname").getValue().toString();
                        textMediaAuthor.setText(authorName);
                        String imageUrl = userSnapshot.child("imageurl").getValue().toString();
                        Picasso.get().load(imageUrl).into(imageMedia);

                        linearMediaList.addView(view);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}