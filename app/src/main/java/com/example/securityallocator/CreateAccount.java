package com.example.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etCPassword;
    private MaterialButton createBtn;
    private FirebaseAuth firebaseAuth;
    private TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();

        firebaseAuth = FirebaseAuth.getInstance();


        createBtn.setOnClickListener(v -> {
            checkFields();
        });

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(CreateAccount.this, Login.class));
            finish();
        });

    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etCPassword = findViewById(R.id.c_password);
        createBtn = findViewById(R.id.create_btn);
        loginBtn = findViewById(R.id.login_btn);
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                startActivity(new Intent(CreateAccount.this, CreateProfile.class));
            }else {
                Toast.makeText(CreateAccount.this, "Couldn't create account", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(CreateAccount.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void checkFields() {
        if (etEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        }else if (etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }else if (etPassword.getText().toString().length() < 8){
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        }else if (!etPassword.getText().toString().equals(etCPassword.getText().toString())) {
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            etCPassword.requestFocus();
            etCPassword.setText("");
        }else {
            createAccount(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
        }
    }


}