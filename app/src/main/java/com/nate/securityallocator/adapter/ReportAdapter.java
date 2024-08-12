package com.nate.securityallocator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nate.securityallocator.R;
import com.nate.securityallocator.models.ReportModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private ArrayList<ReportModel> reportModels;
    private Context context;

    public ReportAdapter(ArrayList<ReportModel> reportModels, Context context) {
        this.reportModels = reportModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportModel model = reportModels.get(position);
        //Picasso.get().load(model.getProfileImg()).into(holder.profileImg);
        //Picasso.get().load(model.getProfileImg()).into(holder.profileImg);
        //Glide.with(context).load(model.getProfileImg).into(holder.profileImg);
        holder.username.setText(model.getUsername());
        holder.email.setText(model.getUserEmail());
        holder.report.setText(model.getReport());
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileImg;
        private TextView username;
        private TextView email;
        private TextView report;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profile_img);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            report = itemView.findViewById(R.id.report);
        }
    }
}
