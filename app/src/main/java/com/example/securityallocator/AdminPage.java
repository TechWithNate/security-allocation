package com.example.securityallocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securityallocator.adapter.UserAdapter;
import com.example.securityallocator.models.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity implements UserAdapter.UserClicked {


    private RecyclerView usersRecycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<UserModel> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        initViews();

        users = new ArrayList<>();
        users.add(new UserModel("nate231@gmail.com", "https://cdn.pixabay.com/photo/2024/05/08/17/45/animal-8748794_1280.jpg", "Nathan", "Dzreke"));
        users.add(new UserModel("billy@gmail.com", "https://cdn.pixabay.com/photo/2022/02/06/14/06/dog-6997211_1280.jpg", "Billy", "Branham"));
        users.add(new UserModel("user@gmail.com", "https://cdn.pixabay.com/photo/2024/02/04/04/11/fashion-8551487_640.jpg", "User", "Jakob"));
        usersRecycler.setHasFixedSize(true);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter adapter = new UserAdapter(users, this, this);
        usersRecycler.setAdapter(adapter);

    }

    private void initViews(){
        usersRecycler = findViewById(R.id.users);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    @Override
    public void userItemClicked(int position) {
        Toast.makeText(this, "User: "+position, Toast.LENGTH_SHORT).show();
        assignTaskUI(users.get(position));
    }

    private void assignTaskUI(UserModel userModel) {
        Intent intent = new Intent(AdminPage.this, JobAssignDetails.class);
        intent.putExtra("user", userModel);
        startActivity(intent);
    }
}