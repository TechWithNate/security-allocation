package com.example.securityallocator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securityallocator.R;
import com.example.securityallocator.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<UserModel> users;
    private Context context;
    private UserClicked userClicked;

    public UserAdapter(ArrayList<UserModel> users, Context context, UserClicked userClicked) {
        this.users = users;
        this.context = context;
        this.userClicked = userClicked;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        UserModel user = users.get(position);
        holder.name.setText(user.getFirstname() + " " +user.getLastname());
        holder.email.setText(user.getEmail());
        Picasso.get().load(user.profileImg).into(holder.profileImg);

        holder.itemView.setOnClickListener(v -> {
            userClicked.userItemClicked(position);
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileImg;
        private TextView name;
        private TextView email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profile_img);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
        }
    }

    public interface UserClicked{
        void userItemClicked(int position);
    }

}
