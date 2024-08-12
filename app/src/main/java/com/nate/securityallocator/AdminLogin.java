package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AdminLogin extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private MaterialButton loginBtn;
    private FirebaseAuth firebaseAuth;
    private TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        initViews();

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {
            loginUser(etEmail.getText().toString(), etPassword.getText().toString());
        });

    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);
    }

    private void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (null != user){
                    if (Objects.equals(user.getEmail(), "dzrekenathan2002@gmail.com")) {
                        Toast.makeText(AdminLogin.this, "Singed In", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminLogin.this, AdminPage.class));
                        finish();
                    }else {
                        Toast.makeText(AdminLogin.this, "Not Authorized", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
    }


}