package com.tgt.triggertactics;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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
import androidx.core.view.ViewCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ImageButton btnBack;

    ImageView imageChatWith;
    TextView txtChatWith;

    EditText editTextMessage;
    Button btnSendMessage;

    LinearLayout linearMessageList;

    String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnBack = findViewById(R.id.btnBack11);
        btnBack.setOnClickListener(v -> finish());

        imageChatWith = findViewById(R.id.imageChatWith);
        txtChatWith = findViewById(R.id.txtChatWith);
        Picasso.get().load(getIntent().getStringExtra("otherImage")).into(imageChatWith);
        txtChatWith.setText(getIntent().getStringExtra("otherName"));

        chatId = getIntent().getStringExtra("chatId");

        editTextMessage = findViewById(R.id.editTextMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        linearMessageList = findViewById(R.id.linearMessageList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference[] msgsRef = {database.getReference("chat/" + chatId + "/messages")};

        btnSendMessage.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty()) {
                editTextMessage.setEnabled(false);

                String newKey = msgsRef[0].push().getKey();

                HashMap<String, Object> messageMap = new HashMap<>();
                messageMap.put("sender", mAuth.getUid());
                messageMap.put("message", message);
                database.getReference("chat/" + chatId + "/messages/" + newKey).updateChildren(messageMap).addOnSuccessListener(unused -> {
                    editTextMessage.setText("");
                    editTextMessage.setEnabled(true);
                });
            }
        });

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        int blueColorInt = Color.parseColor("#2789d9");
        database.getReference("chat/" + chatId + "/messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearMessageList.removeAllViews();
                msgsRef[0] = database.getReference("chat/" + chatId + "/messages");
                for (DataSnapshot msg : snapshot.getChildren()) {
                    String message = msg.child("message").getValue(String.class);
                    String sender = msg.child("sender").getValue(String.class);

                    View view = inflater.inflate(R.layout.layout_chatmessage, linearMessageList, false);
                    TextView txtChatMessage = view.findViewById(R.id.txtChatMessage);
                    LinearLayout messageContainer = view.findViewById(R.id.linearChatMessage);
                    txtChatMessage.setText(message);
                    if (sender.equals(mAuth.getUid())) {
                        ViewCompat.setBackgroundTintList(txtChatMessage, ColorStateList.valueOf(blueColorInt));
                        messageContainer.setGravity(Gravity.END);
                    }
                    linearMessageList.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}