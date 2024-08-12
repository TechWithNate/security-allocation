package com.nate.securityallocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


import com.nate.securityallocator.adapter.ReportAdapter;
import com.nate.securityallocator.models.ReportModel;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {

    private ImageView back;
    private RecyclerView reportRecycler;
    private ArrayList<ReportModel> models;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initViews();
        models = new ArrayList<>();

        models.add(new ReportModel("1", "Dzreke Nathan", "user@ymail.gh", "https://cdn.pixabay.com/photo/2021/11/05/12/27/woman-6771288_1280.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        models.add(new ReportModel("2", "Diving Joa", "user@ymail.gh", "https://cdn.pixabay.com/photo/2019/11/21/15/00/woman-4642701_640.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        models.add(new ReportModel("3", "Alberto Davil", "user@ymail.gh", "https://cdn.pixabay.com/photo/2021/07/21/04/35/woman-6482214_640.jpg", ""+R.string.dummy_txt));
        models.add(new ReportModel("4", "Mickehal Dedy", "user@ymail.gh", "https://cdn.pixabay.com/photo/2017/06/06/16/48/woman-2377742_640.jpg", ""+R.string.dummy_txt));
        models.add(new ReportModel("5", "Dzreke Nathan", "user@ymail.gh", "https://cdn.pixabay.com/photo/2019/08/07/07/05/woman-4390055_640.jpg", ""+R.string.dummy_txt));
        models.add(new ReportModel("6", "Teddy Layyy", "user@ymail.gh", "https://cdn.pixabay.com/photo/2024/02/04/04/11/fashion-8551487_960_720.jpg", ""+R.string.dummy_txt));
        models.add(new ReportModel("7", "Jade Smith", "user@ymail.gh", "https://cdn.pixabay.com/photo/2022/02/06/14/06/dog-6997211_1280.jpg", ""+R.string.dummy_txt));


        reportRecycler.setHasFixedSize(true);
        reportRecycler.setLayoutManager(new LinearLayoutManager(this));
        ReportAdapter adapter = new ReportAdapter(models, this);
        reportRecycler.setAdapter(adapter);

        back.setOnClickListener(v -> {
            startActivity(new Intent(Reports.this, AdminPage.class));
            finish();
        });

    }

    private void initViews(){
        back = findViewById(R.id.back);
        reportRecycler = findViewById(R.id.report_recycler);
    }

}