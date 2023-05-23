package com.tgt.triggertactics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChatListActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ImageButton btnBack;
    LinearLayout linearChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        btnBack = findViewById(R.id.btnBack10);
        btnBack.setOnClickListener(v -> finish());

        linearChatList = findViewById(R.id.linearChatList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String uid = mAuth.getUid();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        database.getReference("chat").get().addOnSuccessListener(sn -> {
            linearChatList.removeAllViews();
            for (DataSnapshot chatSnapshot : sn.getChildren()) {
                if (!chatSnapshot.child("participants").hasChild(uid)) continue;

                String chatId = chatSnapshot.getKey();


                for (DataSnapshot participant : chatSnapshot.child("participants").getChildren()) {
                    if (!participant.getKey().equals(uid)) {
                        String otherUid = participant.getKey();
                        View view = inflater.inflate(R.layout.layout_chatlistitem, linearChatList, false);
                        database.getReference("users/" + otherUid).get().addOnSuccessListener(sn2 -> {
                            String otherName = sn2.child("displayname").getValue(String.class);
                            String otherImage = sn2.child("imageurl").getValue(String.class);

                            ImageButton btnChatImage = view.findViewById(R.id.btnChatImage);
                            Button btnChatName = view.findViewById(R.id.btnChatName);

                            btnChatName.setText(otherName);
                            Picasso.get().load(otherImage).into(btnChatImage);

                            btnChatName.setOnClickListener(l -> {
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                intent.putExtra("chatId", chatId);
                                intent.putExtra("otherName", otherName);
                                intent.putExtra("otherImage", otherImage);
                                startActivity(intent);
                            });
                            btnChatImage.setOnClickListener(l -> {
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                intent.putExtra("chatId", chatId);
                                intent.putExtra("otherName", otherName);
                                intent.putExtra("otherImage", otherImage);
                                startActivity(intent);
                            });
                            linearChatList.addView(view);
                        });
                        break;
                    }
                }
            }
        });
    }
}