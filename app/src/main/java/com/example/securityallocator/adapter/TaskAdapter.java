package com.example.securityallocator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securityallocator.R;
import com.example.securityallocator.models.TaskModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskModel> models;
    private Context context;

    public TaskAdapter(ArrayList<TaskModel> models, Context context) {
        this.models = models;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = models.get(position);
        holder.locationName.setText(task.getLocationName());
        holder.sTime.setText(task.getsTime());
        holder.eTime.setText(task.geteTime());
        holder.date.setText(task.getDate());
        holder.contact.setText(task.getContact());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView locationName;
        private TextView sTime;
        private TextView eTime;
        private TextView contact;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.location_name);
            sTime = itemView.findViewById(R.id.s_time);
            eTime = itemView.findViewById(R.id.e_time);
            contact = itemView.findViewById(R.id.contact);
            date = itemView.findViewById(R.id.date);

        }
    }
}
