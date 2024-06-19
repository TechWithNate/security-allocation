package com.example.securityallocator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securityallocator.R;

public class ScheduleFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_fragment, container, false);
        initViews();


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(){
        recyclerView = view.findViewById(R.id.tasks_recycler);
    }

}
