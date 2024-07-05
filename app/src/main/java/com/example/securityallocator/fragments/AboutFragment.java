package com.example.securityallocator.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.securityallocator.R;

public class AboutFragment extends Fragment {

    private View view;
    private TextView aboutText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_fragment, container, false);
        initView();


        String aboutContent = "<h2>About Security Guard Allocator</h2>" +
                "<p>Welcome to Security Guard Allocator!</p>" +
                "<p>Our app is designed to streamline and simplify the process of assigning security personnel to various locations and tasks. " +
                "Whether you're managing a team of security guards or a single location, Security Guard Allocator provides the tools you need to " +
                "ensure optimal security coverage and efficient task management.</p>" +
                "<h3>Key Features:</h3>" +
                "<ul>" +
                "<li><b>Task Management:</b> Easily create, update, and delete tasks for your security guards.</li>" +
                "<li><b>Real-Time Updates:</b> Keep your team informed with real-time updates and notifications.</li>" +
                "<li><b>User Roles:</b> Assign different roles to users, such as admin or guard, to control access and functionality.</li>" +
                "<li><b>Location-Based Assignments:</b> Allocate tasks based on specific locations for better organization.</li>" +
                "<li><b>Profile Management:</b> Create and manage profiles for each security guard with detailed information.</li>" +
                "<li><b>Navigation Assistance:</b> Provide guards with navigation links to quickly reach their assigned locations.</li>" +
                "</ul>" +
                "<h3>Our Mission:</h3>" +
                "<p>At Security Guard Allocator, our mission is to enhance the efficiency and effectiveness of security management. " +
                "We strive to provide a user-friendly platform that helps security managers allocate resources optimally and ensure the safety of all locations under their watch.</p>" +
                "<h3>Contact Us:</h3>" +
                "<p>If you have any questions, feedback, or need assistance, feel free to reach out to us at <a href='mailto:support@securityguardallocator.com'>support@securityguardallocator.com</a>. We're here to help!</p>" +
                "<p>Thank you for choosing Security Guard Allocator. Your safety and security management are our top priorities.</p>";

        //aboutText.setText(Html.fromHtml(aboutContent, Html.FROM_HTML_MODE_LEGACY));
        aboutText.setText(Html.fromHtml(aboutContent, Html.FROM_HTML_MODE_LEGACY));


        return view;
    }

    private void initView(){
        aboutText = view.findViewById(R.id.about_txt);
    }


}
