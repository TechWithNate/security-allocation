package com.nate.securityallocator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.securityallocator.JobDetails;
import com.nate.securityallocator.R;
import com.nate.securityallocator.adapter.TaskAdapter;
import com.nate.securityallocator.models.TaskModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements TaskAdapter.JobDetailClickListener{

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<TaskModel> tasks;
    private TaskAdapter adapter;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initViews();

        tasks = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(tasks, getContext(), this);
        recyclerView.setAdapter(adapter);

        retrieveJobTasks();



        return view;
    }

    private void initViews(){
        recyclerView = view.findViewById(R.id.tasks_recycler);
        context = view.getContext();
    }

    private void retrieveJobTasks(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("tasks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                TaskModel allTasks = dataSnapshot.getValue(TaskModel.class);
                                if (allTasks != null){
                                    tasks.add(allTasks);
                                }else {
                                    Toast.makeText(getContext(), "No Task", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext(), "No task found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void jobDetailClicked(int position) {
        viewJobDetails(tasks.get(position));
    }

    private void viewJobDetails(TaskModel taskModel) {
        Intent intent = new Intent(context, JobDetails.class);
        intent.putExtra("job", taskModel);
        startActivity(intent);
    }
}
