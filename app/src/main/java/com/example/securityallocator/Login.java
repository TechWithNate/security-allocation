package com.example.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private MaterialButton loginBtn;
    private FirebaseAuth firebaseAuth;
    private TextView signupBtn;
    private TextView adminBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {
            loginUser(etEmail.getText().toString(), etPassword.getText().toString());
        });

        signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, CreateAccount.class));
            finish();
        });
        adminBtn.setOnClickListener(v -> {startActivity(new Intent(Login.this, AdminLogin.class));
            finish();
        });


    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);
        adminBtn = findViewById(R.id.admin_btn);
    }

    private void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(Login.this, "Singed In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, Home.class));
                finish();
            }
        });
    }

}