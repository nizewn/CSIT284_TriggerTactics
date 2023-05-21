package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CreateThreadActivity extends AppCompatActivity {

    Button btnCreateThread, btnCancelCreateThread;
    EditText editThreadTitle, editThreadPost;
    TextView createThreadForumName;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);

        btnCreateThread = findViewById(R.id.btnCreateThread);
        btnCancelCreateThread = findViewById(R.id.btnCancelCreateThread);
        editThreadTitle = findViewById(R.id.editThreadTitle);
        editThreadPost = findViewById(R.id.editThreadPost);

        Intent intent = getIntent();
        String forum = intent.getStringExtra("forum");

        createThreadForumName = findViewById(R.id.createThreadForumName);
        if (forum.equals("feedback"))
            createThreadForumName.setText("FEEDBACK & SUGGESTIONS");
        else if (forum.equals("games"))
            createThreadForumName.setText("OTHER GAMES");
        else
            createThreadForumName.setText(forum.toUpperCase());

        btnCancelCreateThread.setOnClickListener(v -> {
            finish();
        });

        btnCreateThread.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;

            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the date and time formats
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

            // Format the date and time into strings
            String date = now.format(dateFormatter);
            String time = now.format(timeFormatter);

            String key = database.getReference("forums/" + forum).push().getKey();
            Map<String, Object> thread = new HashMap<>();
            thread.put("/forums/" + forum + "/" + key + "/title", editThreadTitle.getText().toString());
            thread.put("/forums/" + forum + "/" + key + "/startedBy", user.getUid());
            thread.put("/forums/" + forum + "/" + key + "/date", date);
            thread.put("/forums/" + forum + "/" + key + "/time", time);
            database.getReference().updateChildren(thread);

            String postKey = database.getReference("forums/" + forum + "/" + key + "/posts").push().getKey();
            Map<String, Object> post = new HashMap<>();
            post.put("/forums/" + forum + "/" + key + "/posts/" + postKey + "/authorId", user.getUid());
            post.put("/forums/" + forum + "/" + key + "/posts/" + postKey + "/content", editThreadPost.getText().toString());

            database.getReference().updateChildren(post);
            finish();
        });
    }
}