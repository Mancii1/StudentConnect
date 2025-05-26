package com.example.student_recuitment_app.Graduate.Profile;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.student_recuitment_app.Application.ApplicationsListActivity;
import com.example.student_recuitment_app.R;
import com.example.student_recuitment_app.authantication.loginActivity;
import com.example.student_recuitment_app.job.JobListActivity;

public class HomegraduateDashboardActivity extends AppCompatActivity {

    String loggedInEmail;

    Button btnProfile, btnAvailableJobs, btnMyApplications, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homegraduate_dashboard);

        btnProfile = findViewById(R.id.btnProfile);
        btnAvailableJobs = findViewById(R.id.btnAvailableJobs);
        btnMyApplications = findViewById(R.id.btnMyApplications);
        btnLogout = findViewById(R.id.btnLogout);

        loggedInEmail = getIntent().getStringExtra("email");

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("email", loggedInEmail); // ✅ Forward email to ProfileActivity
            startActivity(intent);
        });

        btnAvailableJobs.setOnClickListener(v -> {
            Intent intent = new Intent(this, JobListActivity.class);
            intent.putExtra("email", loggedInEmail); // Ensure this exists
            startActivity(intent);
        });

        // Handle button click for btnMyApplications
        btnMyApplications.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApplicationsListActivity.class);
            intent.putExtra("email", loggedInEmail);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, loginActivity.class));
            finish();
        });

    }

}