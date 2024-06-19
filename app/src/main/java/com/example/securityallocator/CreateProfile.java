package com.example.securityallocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.securityallocator.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateProfile extends AppCompatActivity {


    private ImageView profileImage;
    private EditText etFirstname;
    private EditText etLastname;
    private EditText etContact;
    private EditText etAddress;
    private MaterialButton createProfileBtn;
    private ProgressBar progressBar;
    private AutoCompleteTextView genderAutoComplete;
    private String gender;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE = 100;
    private String imageUriAccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        initViews();

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








        createProfileBtn.setOnClickListener(v -> {
            checkFields();
        });

    }

    private void initViews(){
        profileImage = findViewById(R.id.user_img);
        etFirstname = findViewById(R.id.firstname);
        etLastname = findViewById(R.id.lastname);
        etContact = findViewById(R.id.tel);
        etAddress = findViewById(R.id.address);
        genderAutoComplete = findViewById(R.id.gender);
        createProfileBtn = findViewById(R.id.create_profile_btn);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

    }

    private void checkFields(){
        if (imageUri == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        }else if (etFirstname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter firstname", Toast.LENGTH_SHORT).show();
        } else if (etLastname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter lastname", Toast.LENGTH_SHORT).show();
        }else if (genderAutoComplete.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter gender", Toast.LENGTH_SHORT).show();
        } else if (etContact.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter contact", Toast.LENGTH_SHORT).show();
        }else if (etContact.getText().toString().length() < 10){
            Toast.makeText(this, "Contact should not be less than 10", Toast.LENGTH_SHORT).show();
        } else if (etAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
        }else {
            createProfile();
        }
    }

    private void createProfile(){
        progressBar.setVisibility(View.VISIBLE);
        sendDataForNewUser();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(CreateProfile.this, Home.class));
        finish();
    }

    private void sendDataForNewUser() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (null != user){

            //UserModel userProfile = new UserModel(firebaseAuth.getUid(), etFirstname.getText().toString(), etLastname.getText().toString(), gender.toLowerCase(), etContact.getText().toString(), etAddress.getText().toString());
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("userUID", firebaseAuth.getCurrentUser().getUid());
            userProfile.put("firstname", etFirstname.getText().toString());
            userProfile.put("lastname", etLastname.getText().toString());
            userProfile.put("gender", gender.toLowerCase());
            userProfile.put("contact", etContact.getText().toString());
            userProfile.put("address", etAddress.getText().toString());


            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid()))
                    .updateChildren(userProfile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CreateProfile.this, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CreateProfile.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        sendImageToStorage();

    }

    private void sendImageToStorage() {
        storageReference.child("Images").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Profile Pic");

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot ->
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            imageUriAccessToken = uri.toString();
            Map<String, Object> image = new HashMap<>();
            image.put("imageURI", imageUriAccessToken);
            databaseReference.updateChildren(image).addOnSuccessListener(unused ->
                    Toast.makeText(CreateProfile.this, "Image Loaded", Toast.LENGTH_SHORT).show());
        }).addOnFailureListener(e ->
                        Toast.makeText(CreateProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}