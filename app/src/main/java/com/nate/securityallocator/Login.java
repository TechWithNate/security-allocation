package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private MaterialButton loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private TextView signupBtn;
    private TextView adminBtn;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        loginBtn.setOnClickListener(v -> {
            loginUser(etEmail.getText().toString(), etPassword.getText().toString());
        });

        signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, CreateAccount.class));
            finish();
        });



    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

    // Login User Based on Role
    private void loginUser(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
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
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    // Check User Role
    private void checkUserRole(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if (role != null) {
                        if (role.equals("admin")) {
                            startActivity(new Intent(Login.this, AdminPage.class));
                        } else {
                            startActivity(new Intent(Login.this, Home.class));
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

}