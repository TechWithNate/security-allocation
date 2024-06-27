package com.example.securityallocator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.securityallocator.EditProfile;
import com.example.securityallocator.R;
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

import org.w3c.dom.Text;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    
    private View view;
    private ImageView profileImage;
    private TextView tvUsername;
    private TextView tvEmail;
    private EditText etFirstname;
    private EditText etLastname;
    private AutoCompleteTextView genderAutoComplete;
    private TextView etContact;
    private TextView etAddress;
    private MaterialButton editProfileBtn;
    private MaterialButton logoutBtn;



    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        initViews();

        fetchUserProfile();
        editProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), EditProfile.class));
        });

        return view;
    }

    private void initViews(){
        profileImage = view.findViewById(R.id.profile_img);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        etFirstname = view.findViewById(R.id.firstname);
        etLastname = view.findViewById(R.id.lastname);
        etContact = view.findViewById(R.id.tel);
        etAddress = view.findViewById(R.id.et_address);
        genderAutoComplete = view.findViewById(R.id.gender);
        editProfileBtn = view.findViewById(R.id.edit_profile_btn);

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
                        Toast.makeText(getContext(), "Profile does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to fetch profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImageFromUri(String imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(profileImage);
    }


    private void displayCurrentUser(){
        if (firebaseUser != null){
            DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
            tasksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot taskSnapshot: snapshot.getChildren()){
                        String username = taskSnapshot.child("role").getValue(String.class);
                        tvUsername.setText(taskSnapshot.child("username").getValue(String.class));
                        tvEmail.setText(taskSnapshot.child("email").getValue(String.class));
                        Toast.makeText(getContext(), "name: "+username, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getContext(), "Null User", Toast.LENGTH_SHORT).show();
        }

    }
    
}
