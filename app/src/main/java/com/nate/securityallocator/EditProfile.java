package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {


    private ImageView profileImage;
    private TextView tvUsername;
    private TextView tvEmail;
    private EditText etFirstname;
    private EditText etLastname;
    private AutoCompleteTextView genderAutoComplete;
    private TextView etContact;
    private TextView etAddress;
    private Uri imageUri;
    private MaterialButton editProfileBtn;
    private ProgressBar progressBar;
    private MaterialButton logoutBtn;



    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private static final int PICK_IMAGE = 100;
    private String imageUriAccessToken;
    private String gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();

        fetchUserProfile();

        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderAutoComplete.setAdapter(adapter);
        genderAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            gender = (String) parent.getItemAtPosition(position);
        });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });


        editProfileBtn.setOnClickListener(v -> updateUserProfile());

    }


    private void initViews(){
        profileImage = findViewById(R.id.profile_img);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        etFirstname = findViewById(R.id.firstname);
        etLastname = findViewById(R.id.lastname);
        etContact = findViewById(R.id.tel);
        etAddress = findViewById(R.id.et_address);
        genderAutoComplete = findViewById(R.id.gender);
        editProfileBtn = findViewById(R.id.edit_profile_btn);
        progressBar = findViewById(R.id.progress_bar);

        // In your onCreate or a similar method
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("Images").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Profile Pic");

    }


    private void fetchUserProfile() {
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Extract user profile data
                        String username = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String lastname = snapshot.child("lastname").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String contact = snapshot.child("contact").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String imageUri = snapshot.child("imageURI").getValue(String.class);

                        // Populate UI components
                        tvUsername.setText(username);
                        tvEmail.setText(email);
                        genderAutoComplete.setText(gender);
                        etFirstname.setText(firstname);
                        etLastname.setText(lastname);
                        etContact.setText(contact);
                        etAddress.setText(address);
                        if (imageUri != null && !imageUri.isEmpty()) {
                            loadImageFromUri(imageUri);
                        }
                    } else {
                        Toast.makeText(EditProfile.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfile.this, "Failed to fetch profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImageFromUri(String imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(profileImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void updateUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Get updated values from UI components
            String updatedFirstname = etFirstname.getText().toString().trim();
            String updatedLastname = etLastname.getText().toString().trim();
            String updatedGender = gender;
            String updatedContact = etContact.getText().toString().trim();
            String updatedAddress = etAddress.getText().toString().trim();

            // Create a map with the updated values
            Map<String, Object> updatedUserProfile = new HashMap<>();
            updatedUserProfile.put("firstname", updatedFirstname);
            updatedUserProfile.put("lastname", updatedLastname);
            updatedUserProfile.put("gender", updatedGender);
            updatedUserProfile.put("contact", updatedContact);
            updatedUserProfile.put("address", updatedAddress);

            if (etFirstname.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter firstname", Toast.LENGTH_SHORT).show();
            }else if (etLastname.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter lastname", Toast.LENGTH_SHORT).show();
            }else if (genderAutoComplete.getText().toString().isEmpty()){
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
            }else if (etContact.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter Contact", Toast.LENGTH_SHORT).show();
            }else if (etAddress.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            }else {
                // Update the values in the database
                databaseReference.child(userId).updateChildren(updatedUserProfile).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If image needs to be updated, call the method to handle image upload
                        if (imageUri != null) {
                            uploadProfileImage(userId);
                        } else {
                            goToHomePage();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfile.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }


        } else {
            Toast.makeText(EditProfile.this, "User is not logged in", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void goToHomePage() {
        startActivity(new Intent(EditProfile.this, Home.class));
        finish();
    }

    private void uploadProfileImage(String userId) {
        storageReference.child("Images").child(userId).child("Profile Pic").putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageReference.child("Images").child(userId).child("Profile Pic").getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUriAccessToken = uri.toString();
                    Map<String, Object> imageUpdate = new HashMap<>();
                    imageUpdate.put("imageURI", imageUriAccessToken);
                    databaseReference.child(userId).updateChildren(imageUpdate).addOnSuccessListener(unused -> {
                        goToHomePage();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProfile.this, "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfile.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
        ).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(EditProfile.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


}