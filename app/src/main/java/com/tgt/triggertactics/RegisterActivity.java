package com.tgt.triggertactics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    Button btnCreateAcc;
    EditText registerDisplayName, registerEmail, registerPassword, registerConfirmPassword;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerDisplayName = findViewById(R.id.registerDisplayName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);

        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        btnCreateAcc.setOnClickListener(view -> {
            if (registerDisplayName.getText().length() > 1 && registerEmail.getText().length() > 1 && registerPassword.getText().length() > 1 && registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())) {
                createAccount(registerDisplayName.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString());
            }
        });
    }

    private void createAccount(String displayName, String email, String password) {
        btnCreateAcc.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        String defaultPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/triggertactics-4c472.appspot.com/o/default_pfp.jpg?alt=media&token=ee5d9425-b219-42b7-8c79-c765beadda9f";

                        DatabaseReference ref = database.getReference("users/" + user.getUid());
                        ref.child("displayname").setValue(displayName);
                        ref.child("reputation").setValue(0);
                        ref.child("imageurl").setValue(defaultPhotoUrl);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .setPhotoUri(Uri.parse(defaultPhotoUrl))
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                        btnCreateAcc.setEnabled(true);
                    }
                });
    }
}