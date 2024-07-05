package com.example.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securityallocator.adapter.UserAdapter;
import com.example.securityallocator.models.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminPage extends AppCompatActivity implements UserAdapter.UserClicked {


    private MaterialToolbar topAppBar;
    private RecyclerView usersRecycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<UserModel> users;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        initViews();

        topAppBar.setOnMenuItemClickListener(item -> {
            int menuID = item.getItemId();
            if (menuID == R.id.logout) {
                firebaseAuth.signOut();
                startActivity(new Intent(AdminPage.this, Login.class));
                finish();
                return true;
            } else if (menuID == R.id.report) {
                startActivity(new Intent(AdminPage.this, Reports.class));
            }else {
                return false;
            }
            return true;
        });

        users = new ArrayList<>();

        usersRecycler.setHasFixedSize(true);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(users, this, this);
        usersRecycler.setAdapter(adapter);
        fetchUsersWithUserRole();

    }

    private void initViews(){
        usersRecycler = findViewById(R.id.users);
        topAppBar = findViewById(R.id.topAppBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    @Override
    public void userItemClicked(int position) {
        assignTaskUI(users.get(position));
    }

    private void assignTaskUI(UserModel userModel) {
        Intent intent = new Intent(AdminPage.this, JobAssignDetails.class);
        intent.putExtra("user", userModel);
        startActivity(intent);
    }


    private void fetchUsersWithUserRole() {

        Query query = databaseReference.child("users").orderByChild("role").equalTo("user");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        UserModel userModel = userSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            users.add(userModel);
                        }
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "No users found with the role 'user'", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch users: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




}