package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ThreadListActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView textViewForumName;
    Button btnCreateThreadActivity;

    LinearLayout linearThreadList;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);

        Intent intent = getIntent();
        String forum = intent.getStringExtra("forum");

        btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(view -> finish());

        btnCreateThreadActivity = findViewById(R.id.btnCreateThreadActivity);
        btnCreateThreadActivity.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), CreateThreadActivity.class);
            intent1.putExtra("forum", forum);
            startActivity(intent1);
        });

        linearThreadList = findViewById(R.id.linearThreadList);

        textViewForumName = findViewById(R.id.textViewForumName);
        if (forum.equals("feedback"))
            textViewForumName.setText("FEEDBACK & SUGGESTIONS");
        else if (forum.equals("games"))
            textViewForumName.setText("OTHER GAMES");
        else
            textViewForumName.setText(forum.toUpperCase());
    }

    @Override
    protected void onStart() {
        super.onStart();

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        linearThreadList.removeAllViews();

        Intent intent = getIntent();
        String forum = intent.getStringExtra("forum");

        DatabaseReference ref = database.getReference("forums/" + forum);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot threadSnapshot : task.getResult().getChildren()) {
                    String threadKey = threadSnapshot.getKey();
                    String threadTitle = threadSnapshot.child("title").getValue().toString();
                    String threadAuthorId = threadSnapshot.child("startedBy").getValue().toString();
                    String threadDate = threadSnapshot.child("date").getValue().toString();
                    String threadTime = threadSnapshot.child("time").getValue().toString();

                    DatabaseReference userRef = database.getReference("users/" + threadAuthorId);
                    userRef.get().addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful()) {
                            String threadAuthor = userTask.getResult().child("displayname").getValue().toString();
                            String threadAuthorImageUrl = userTask.getResult().child("imageurl").getValue().toString();

                            View threadItemView = inflater.inflate(R.layout.layout_thread, linearThreadList, false);
                            Button textViewThreadTitle = threadItemView.findViewById(R.id.btnThreadTitle);
                            ImageButton imageThreadButton = threadItemView.findViewById(R.id.imageThreadButton);
                            TextView threadCaption = threadItemView.findViewById(R.id.threadCaptionInList);

                            textViewThreadTitle.setText(threadTitle);
                            threadCaption.setText(threadAuthor + " - " + threadDate + " " + threadTime);
                            Picasso.get().load(threadAuthorImageUrl).into(imageThreadButton);

                            imageThreadButton.setOnClickListener(view -> {
                                Intent intent1 = new Intent(getApplicationContext(), ThreadActivity.class);
                                intent1.putExtra("forum", forum);
                                intent1.putExtra("threadTitle", threadTitle);
                                intent1.putExtra("threadKey", threadKey);
                                startActivity(intent1);
                            });

                            textViewThreadTitle.setOnClickListener(view -> {
                                Intent intent1 = new Intent(getApplicationContext(), ThreadActivity.class);
                                intent1.putExtra("forum", forum);
                                intent1.putExtra("threadTitle", threadTitle);
                                intent1.putExtra("threadKey", threadKey);
                                startActivity(intent1);
                            });

                            linearThreadList.addView(threadItemView);

                        }
                    });
                }
            }
        });
    }
}