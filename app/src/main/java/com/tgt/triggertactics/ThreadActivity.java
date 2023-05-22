package com.tgt.triggertactics;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ThreadActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String forum;
    String threadKey;
    String threadTitle;

    TextView textViewThreadTitle, textViewFromForum;
    ImageButton btnBack;
    LinearLayout linearPostList;

    EditText editTextNewPost;
    Button btnPostInThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        forum = getIntent().getStringExtra("forum");
        threadKey = getIntent().getStringExtra("threadKey");
        threadTitle = getIntent().getStringExtra("threadTitle");

        textViewThreadTitle = findViewById(R.id.textViewThreadTitle);
        textViewThreadTitle.setText(threadTitle);

        textViewFromForum = findViewById(R.id.textViewFromForum);
        textViewFromForum.setText(forum.toUpperCase());

        linearPostList = findViewById(R.id.linearPostList);

        btnBack = findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(view -> finish());

        editTextNewPost = findViewById(R.id.editTextNewPost);
        btnPostInThread = findViewById(R.id.btnPostInThread);

        btnPostInThread.setOnClickListener(view -> {
            btnPostInThread.setEnabled(false);
            String content = editTextNewPost.getText().toString();
            String authorId = mAuth.getCurrentUser().getUid();

            DatabaseReference ref = database.getReference("forums/" + forum + "/" + threadKey + "/posts");
            String newPostKey = ref.push().getKey();
            Map<String, Object> post = new HashMap<>();
            post.put("/forums/" + forum + "/" + threadKey + "/posts/" + newPostKey + "/authorId", authorId);
            post.put("/forums/" + forum + "/" + threadKey + "/posts/" + newPostKey + "/content", content);

            database.getReference().updateChildren(post).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    editTextNewPost.setText("");
                }
                btnPostInThread.setEnabled(true);
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        database.getReference("forums/" + forum + "/" + threadKey + "/posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearPostList.removeAllViews();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String content = postSnapshot.child("content").getValue().toString();
                    String authorId = postSnapshot.child("authorId").getValue().toString();

                    DatabaseReference ref = database.getReference("users/" + authorId);
                    ref.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String authorName = task.getResult().child("displayname").getValue().toString();
                            String authorImage = task.getResult().child("imageurl").getValue().toString();

                            View view = inflater.inflate(R.layout.layout_threadpost, linearPostList, false);

                            ImageView textPostImage = view.findViewById(R.id.textPostImage);
                            TextView textPostAuthor = view.findViewById(R.id.textPostAuthor);
                            TextView textPostContent = view.findViewById(R.id.textPostContent);

                            Picasso.get().load(authorImage).into(textPostImage);
                            textPostAuthor.setText(authorName);
                            textPostContent.setText(content);

                            linearPostList.addView(view);
                        }
                    });
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}