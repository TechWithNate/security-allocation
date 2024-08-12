package com.nate.securityallocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.nate.securityallocator.models.TaskModel;

import java.util.ArrayList;

public class JobDetails extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private TextView location;
    private TextView startTime;
    private TextView endTime;
    private TextView startDate;
    private TextView endDate;
    private TextView shift;
    private MaterialCardView report;
    private MaterialButton mapBtn;
    private MaterialButton callBtn;
    private TaskModel taskModels;
    private String locationUrl;
    private String contact;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        initViews();

        taskModels = getIntent().getParcelableExtra("job");

        if (taskModels != null){
            location.setText(taskModels.getLocationName());
            startTime.setText(taskModels.getsTime());
            endTime.setText(taskModels.geteTime());
            startDate.setText(taskModels.getsDate());
            endDate.setText(taskModels.geteDate());
            shift.setText(taskModels.getShift());
            locationUrl = taskModels.getLocationUrl();
            contact = taskModels.getContact();
        }else {
            Toast.makeText(this, "Null Task", Toast.LENGTH_SHORT).show();
        }

        mapBtn.setOnClickListener(v -> {
            if (!locationUrl.isEmpty()){
                openMap("google.navigation:q="+locationUrl);
            }else {
                Toast.makeText(this, "No Map Url", Toast.LENGTH_SHORT).show();
            }
        });


        callBtn.setOnClickListener(v -> {
            checkCallPermissionAndMakeCall();
        });

        back.setOnClickListener(v -> {
            finish();
        });

    }

    private void initViews(){
        location = findViewById(R.id.location);
        startTime = findViewById(R.id.s_time);
        endTime = findViewById(R.id.e_time);
        startDate = findViewById(R.id.s_date);
        endDate = findViewById(R.id.e_date);
        shift = findViewById(R.id.shift);
        mapBtn = findViewById(R.id.map_btn);
        callBtn = findViewById(R.id.call_btn);
        report = findViewById(R.id.report);
        back = findViewById(R.id.back);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openMap(String link) {
        Uri gmmIntentUri = Uri.parse(link);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    private void checkCallPermissionAndMakeCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Permission already granted, make the call
            makePhoneCall();
        }
    }

    private void makePhoneCall() {
        if (!contact.isEmpty()){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                makePhoneCall();
            } else {
                // Permission denied
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}