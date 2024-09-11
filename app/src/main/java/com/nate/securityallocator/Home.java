package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nate.securityallocator.fragments.AboutFragment;
import com.nate.securityallocator.fragments.HomeFragment;
import com.nate.securityallocator.fragments.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Home extends AppCompatActivity {


    private ImageView profileImage;
    private TextView username;
    private TextView fullName;
    private TextView email;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    private NavigationView navigationView;

    private DrawerLayout drawerLayout;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();




        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.email);
        profileImage = headerView.findViewById(R.id.profile_img);
        fetchUserProfile();


        toolbar.setTitle("Home");

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);

                if (itemId == R.id.nav_home) {
                    toolbar.setTitle("Home");
                    replaceFragment(new HomeFragment());
                }else if (itemId == R.id.nav_profile){
                    // TODO Open Profile
                    toolbar.setTitle("Profile");
                    replaceFragment(new ProfileFragment());
                   // Toast.makeText(Home.this, "This is: "+item.getTitle(), Toast.LENGTH_SHORT).show();
                }else if (itemId == R.id.nav_share) {
                    String shareText = "Check out this link: https://www.example.com";

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                    startActivity(Intent.createChooser(shareIntent, "Share link using"));
                } else if (itemId == R.id.nav_about) {
                    // About Us
                    //Toast.makeText(Home.this, "This is: "+item.getTitle(), Toast.LENGTH_SHORT).show();
                    toolbar.setTitle("About");
                    replaceFragment(new AboutFragment());
                } else if (itemId == R.id.nav_logout) {
                    // Logout User
                    firebaseAuth.signOut();
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                    Toast.makeText(Home.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initViews(){


        navigationView = findViewById(R.id.nav_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //storageReference = FirebaseStorage.getInstance().getReference("Images").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Profile Pic");

    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void fetchUserProfile() {
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Extract user profile data;

                        // Populate UI components
                        String db_username = snapshot.child("username").getValue(String.class);
                        String db_email = snapshot.child("email").getValue(String.class);
                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String lastname = snapshot.child("lastname").getValue(String.class);
                        String imageUri = snapshot.child("profileImg").getValue(String.class);

                        // Populate UI components
                        username.setText(firstname + " " + lastname);
                        email.setText(db_email);

                        if (imageUri != null && !imageUri.isEmpty()) {
                            loadImageFromUri(imageUri);
                        }
                    } else {
                        Toast.makeText(Home.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Home.this, "Failed to fetch profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImageFromUri(String imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(profileImage);
    }





}