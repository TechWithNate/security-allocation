package com.example.securityallocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.securityallocator.models.TaskModel;
import com.example.securityallocator.models.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class JobAssignDetails extends AppCompatActivity {


    private ImageView back;
    private ImageView profileImg;
    private TextView title;
    private ProgressBar progressBar;
    private TextView username;
    private TextView email;
    private EditText locationName;
    private EditText locationUrl;
    private EditText contact;
    private MaterialCardView sDateLayout;
    private MaterialCardView eDateLayout;
    private MaterialButton assignBtn;
    private AutoCompleteTextView autoCompleteShift;
    private String shift;
    private Calendar calendar;
    private TextView sEtDate;
    private TextView sEtDay;

    private TextView eEtDate;
    private TextView eEtDay;
    private RelativeLayout startTime;
    private RelativeLayout endTime;
    private int selectedHourStart;
    private int selectedMinuteStart;
    private int selectedHourEnd;
    private int selectedMinuteEnd;
    private TextView startTimeText;
    private TextView endTimeText;
    private UserModel user;
    private String userID;
    private String sTime;
    private String eTime;
    private String sDate;
    private String eDate;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_assign_details);
        initViews();


        back.setOnClickListener(v -> finish());

        user = getIntent().getParcelableExtra("user");
        if (null != user){
            username.setText(user.getFirstname()+ " "+ user.getLastname());
            title.setText("Assign "+ user.getFirstname());
            email.setText(user.getEmail());
            userID = user.getUserUID();
            String profilePicture = user.getProfileImg();
            if (null != profilePicture){
                Picasso.get().load(profilePicture).into(profileImg);
            }else {
                Toast.makeText(this, "User Image not found", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
        }

        String[] shiftOptions = {"Day", "Night"};
        ArrayAdapter<String> shiftAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shiftOptions);
        autoCompleteShift.setAdapter(shiftAdapter);
        autoCompleteShift.setOnItemClickListener((parent, view, position, id) -> {
            shift = (String) parent.getItemAtPosition(position);
        });


        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set click listeners for the date layouts
        sDateLayout.setOnClickListener(v -> showDatePickerDialog(sEtDate, sEtDay, year, month, day));
        eDateLayout.setOnClickListener(v -> showDatePickerDialog(eEtDate, eEtDay, year, month, day));

        startTime.setOnClickListener(v -> showTimePickerDialog(true));

        endTime.setOnClickListener(v -> showTimePickerDialog(false));

        assignBtn.setOnClickListener(v -> sendToUserDB());



    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);
        profileImg = findViewById(R.id.profile_img);
        title = findViewById(R.id.title);
        username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        locationName = findViewById(R.id.location_name);
        locationUrl = findViewById(R.id.location_url);
        contact = findViewById(R.id.contact);
        autoCompleteShift = findViewById(R.id.shift);
        assignBtn = findViewById(R.id.assign_btn);
        sDateLayout = findViewById(R.id.start_date_layout);
        eDateLayout = findViewById(R.id.end_date_layout);
        sEtDate = findViewById(R.id.etDate);
        sEtDay = findViewById(R.id.etDay);
        eEtDate = findViewById(R.id.e_etDate);
        eEtDay = findViewById(R.id.e_etDay);
        startTime = findViewById(R.id.start_time_layout);
        endTime = findViewById(R.id.end_time_layout);
        startTimeText = findViewById(R.id.start_time);
        endTimeText = findViewById(R.id.end_time);
    }


    private void showDatePickerDialog(TextView dateField, TextView dayField, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener setListener = (view, yearSelected, monthSelected, dayOfMonthSelected) -> {
            calendar.set(yearSelected, monthSelected, dayOfMonthSelected);
            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            String formattedDate = dateFormat.format(date);
            String formattedDay = dayFormat.format(date);

            dateField.setText(formattedDate);
            dayField.setText(formattedDay);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                JobAssignDetails.this, android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day
        );
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final boolean isStartTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isStartTime) {
                            selectedHourStart = hourOfDay;
                            selectedMinuteStart = minute;
                            startTimeText.setText(formatTime(selectedHourStart, selectedMinuteStart));
                            sTime = formatTime(selectedHourStart, selectedMinuteStart);
                        } else {
                            selectedHourEnd = hourOfDay;
                            selectedMinuteEnd = minute;
                            endTimeText.setText(formatTime(selectedHourEnd, selectedMinuteEnd));
                            eTime = formatTime(selectedHourEnd, selectedMinuteEnd);

                        }
                    }
                }, hour, minute, false);



        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        String time;
        String period = "AM";
        if (hourOfDay >= 12) {
            period = "PM";
            if (hourOfDay != 12) {
                hourOfDay -= 12;
            }
        }
        if (hourOfDay == 0) {
            hourOfDay = 12;
        }
        time = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, period);
        return time;
    }

    private void sendToUserDB(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String locName = locationName.getText().toString();
        String locUrl = locationUrl.getText().toString();
        String sDate = eEtDate.getText().toString();
        String eDate = eEtDate.getText().toString();
        String tel =  contact.getText().toString();

        if (locName.isEmpty()){
            Toast.makeText(this, "Enter location name", Toast.LENGTH_SHORT).show();
        }else if (locUrl.isEmpty()){
            Toast.makeText(this, "Enter location URL", Toast.LENGTH_SHORT).show();
        }else if (sDate.isEmpty()){
            Toast.makeText(this, "Enter Start Date", Toast.LENGTH_SHORT).show();
        }else if (eDate.isEmpty()){
            Toast.makeText(this, "Enter end date", Toast.LENGTH_SHORT).show();
        }else if (tel.isEmpty()){
            Toast.makeText(this, "Enter telephone number", Toast.LENGTH_SHORT).show();
        }else {
            String taskID = databaseReference.push().getKey();
            TaskModel task = new TaskModel(taskID, userID, locName, locUrl, shift,sTime, eTime, sDate, eDate, tel);

            if (taskID != null){
                databaseReference.child("users").child(userID).child("tasks").child(taskID).setValue(task)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toast.makeText(JobAssignDetails.this, "Task Added for "+username.getText().toString()+" Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                progressBar.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }else {
                Toast.makeText(JobAssignDetails.this, "Failed to create a task ID", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }


    }

}