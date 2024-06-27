package com.example.securityallocator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securityallocator.R;
import com.example.securityallocator.adapter.TaskAdapter;
import com.example.securityallocator.models.TaskModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<TaskModel> tasks;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initViews();

        tasks = new ArrayList<>();
        tasks.add(new TaskModel("1", "Accra Poly", "", "12:00", "03:00", "12/02/2024","0546651113"));
        tasks.add(new TaskModel("2", "University of Ghana", "", "09:00", "16:00", "11/02/2024", "0541234567"));
        tasks.add(new TaskModel("3", "Kwame Nkrumah University", "", "10:00", "15:00", "10/02/2024", "0245678901"));
        tasks.add(new TaskModel("4", "Accra Mall", "", "14:00", "18:00", "13/02/2024", "0543219876"));
        tasks.add(new TaskModel("5", "Legon Hospital", "", "08:00", "12:00", "09/02/2024", "0265432111"));
        tasks.add(new TaskModel("6", "Madina Market", "", "11:00", "17:00", "12/02/2024", "0547654321"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TaskAdapter adapter = new TaskAdapter(tasks, getContext());
        recyclerView.setAdapter(adapter);




        return view;
    }

    private void initViews(){
        recyclerView = view.findViewById(R.id.tasks_recycler);
    }

}
