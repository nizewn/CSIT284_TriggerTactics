package com.tgt.triggertactics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://triggertactics-4c472-default-rtdb.asia-southeast1.firebasedatabase.app/");
    ImageButton editProfileImageButton;
    EditText editDisplayName, editEmail, editNewPassword;
    Button btnSaveProfile, btnEditGamesActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        editProfileImageButton = findViewById(R.id.editProfileImageButton);
        editDisplayName = findViewById(R.id.editDisplayName);
        editEmail = findViewById(R.id.editEmail);
        editNewPassword = findViewById(R.id.editNewPassword);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnEditGamesActivity = findViewById(R.id.btnEditGamesActivity);

        btnEditGamesActivity.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditGamesActivity.class);
            startActivity(intent);
        });
        editProfileImageButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            System.out.println("Selecting image...");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });
        btnSaveProfile.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            if (!editDisplayName.getText().toString().equals(user.getDisplayName())) {
                user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(editDisplayName.getText().toString()).build());
                DatabaseReference ref = database.getReference("users/" + user.getUid());
                ref.child("displayname").setValue(editDisplayName.getText().toString());
            }
            if (!editEmail.getText().toString().equals(user.getEmail())) {
                user.updateEmail(editEmail.getText().toString());
            }
            if (!editNewPassword.getText().toString().equals("")) {
                user.updatePassword(editNewPassword.getText().toString());
            }
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Handle the selected image URI to firebase cloud storage
            System.out.println(imageUri);
            StorageReference storageRef = storage.getReference();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            StorageReference profileImageRef = storageRef.child("profileImages/" + mAuth.getCurrentUser().getUid() + "_" + imageUri.getLastPathSegment());
            profileImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Get a URL to the uploaded content
                profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update the user's profile image
                    user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder().setPhotoUri(uri).build());
                    DatabaseReference ref = database.getReference("users/" + user.getUid());
                    ref.child("imageurl").setValue(uri.toString());
                    Picasso.get().load(uri).into(editProfileImageButton);
                });
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            editDisplayName.setText(currentUser.getDisplayName());
            editEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Picasso.get().load(currentUser.getPhotoUrl()).into(editProfileImageButton);
            }
        }
    }
}