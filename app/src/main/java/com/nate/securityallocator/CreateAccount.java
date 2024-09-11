package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.securityallocator.models.UserModel;

import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etCPassword;
    private ProgressBar progressBar;
    private MaterialButton createBtn;
    private FirebaseAuth firebaseAuth;
    private TextView loginBtn;
    private AutoCompleteTextView roleAutoComplete;
    private EditText etUsername;
    private String role;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


//        email = etEmail.getText().toString().trim();
//        password = etPassword.getText().toString();
//        username = etUsername.getText().toString();


        // User role options
        String[] roleOptions = {"Admin", "User"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roleOptions);
        roleAutoComplete.setAdapter(roleAdapter);
        roleAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            role = (String) parent.getItemAtPosition(position);
        });


        //Create Account
        createBtn.setOnClickListener(v -> {
            checkFields();
        });

        // Navigate to login Page
        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(CreateAccount.this, Login.class));
            finish();
        });

    }

    // Initializing Views
    private void initViews() {
        etUsername = findViewById(R.id.username);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etCPassword = findViewById(R.id.c_password);
        createBtn = findViewById(R.id.create_btn);
        loginBtn = findViewById(R.id.login_btn);
        roleAutoComplete = findViewById(R.id.role);
        progressBar = findViewById(R.id.progress_bar);
    }


    // Create Account
    private void createAccount(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (null != user) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(etUsername.getText().toString())
                            .build();
                    user.updateProfile(profileChangeRequest);

                    UserModel newUser = new UserModel(etUsername.getText().toString(), etEmail.getText().toString(), role.toLowerCase());
                    mDatabase.child(user.getUid()).setValue(newUser);
                }
                //startActivity(new Intent(CreateAccount.this, CreateProfile.class));
                // TODO: Login User
                loginUser(etEmail.getText().toString(), etPassword.getText().toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateAccount.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateAccount.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }

    private void checkUserRole(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if (role != null) {
                        if (role.equals("admin")) {
                            Intent intent = new Intent(CreateAccount.this, AdminPage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                if (!Objects.requireNonNull(dataSnapshot.getValue(String.class)).isEmpty()){
                                    Intent intent = new Intent(CreateAccount.this, Home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
//                            Intent intent = new Intent(CreateAccount.this, CreateProfile.class);
//                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to get user role!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to get user role: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void checkFields() {
        if (etUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
        } else if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().length() < 8) {
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().equals(etCPassword.getText().toString())) {
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            etCPassword.requestFocus();
            etCPassword.setText("");
        } else if (roleAutoComplete.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Role", Toast.LENGTH_SHORT).show();
        } else {
            createAccount(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
        }
    }


    // Directly Login User Based on Role
    private void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (null != user){
                             checkUserRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Check User Role


    @Override
    protected void onStart() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (null != user){
            checkUserRole(firebaseAuth.getUid());
//            Intent intent = new Intent(CreateAccount.this, Home.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
        }

        super.onStart();
    }

}