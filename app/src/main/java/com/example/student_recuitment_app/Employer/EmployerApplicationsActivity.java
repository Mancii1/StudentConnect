package com.example.student_recuitment_app.Employer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.Application.JobApplication;
import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.util.List;

public class EmployerApplicationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmployerApplicationsAdapter adapter;
    private DatabaseHelper dbHelper;
    private int employerId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employer_applications);

        employerId = getIntent().getIntExtra("employer_id", -1);
        if(employerId == -1) {
            Toast.makeText(this, "Employer not recognized", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<JobApplication> applications = dbHelper.getApplicationsForEmployer(employerId);
        adapter = new EmployerApplicationsAdapter(applications, new EmployerApplicationsAdapter.OnStatusChangeListener() {
            @Override
            public void onStatusChanged(int applicationId, String newStatus) {
                boolean success = dbHelper.updateApplicationStatus(applicationId, newStatus);
                if(success) {
                    refreshApplications();
                    Toast.makeText(EmployerApplicationsActivity.this,
                            "Status updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void refreshApplications() {
        List<JobApplication> updated = dbHelper.getApplicationsForEmployer(employerId);
        adapter.updateData(updated);
    }
}