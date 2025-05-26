package com.example.student_recuitment_app.job;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends AppCompatActivity {

    private RecyclerView jobsRecyclerView;
    private JobAdapter adapter;
    private DatabaseHelper dbHelper;
    private EditText searchEditText;
    private List<Job> allJobs;
    private String loggedInEmail;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loggedInEmail = getIntent().getStringExtra("email");
        if (loggedInEmail == null) {
            Toast.makeText(this, "Error: User session not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        jobsRecyclerView = findViewById(R.id.jobsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        emptyView = findViewById(R.id.emptyView);

        setupRecyclerView();
        setupSearchListener();
    }

    private void setupRecyclerView() {
        try {
            allJobs = dbHelper.getAllJobs();

            if (allJobs == null || allJobs.isEmpty()) {
                showEmptyState();
                return;
            }

            adapter = new JobAdapter(
                    allJobs,
                    job -> {
                        Intent intent = new Intent(this, JobDetailsActivity.class);
                        intent.putExtra("job_id", job.getId());
                        intent.putExtra("email", loggedInEmail);
                        startActivity(intent);
                    },
                    loggedInEmail,
                    dbHelper
            );

            jobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            jobsRecyclerView.setAdapter(adapter);
            showJobsList();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading jobs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            showEmptyState();
        }
    }

    private void showEmptyState() {
        jobsRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showJobsList() {
        jobsRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterJobs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterJobs(String query) {
        if (allJobs == null) return;
        
        query = query.toLowerCase();
        List<Job> filtered = new ArrayList<>();
        
        for (Job job : allJobs) {
            if (job == null) continue;
            
            String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
            String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";
            String type = job.getType() != null ? job.getType().toLowerCase() : "";

            if (title.contains(query) || location.contains(query) || type.contains(query)) {
                filtered.add(job);
            }
        }
        
        if (filtered.isEmpty()) {
            showEmptyState();
        } else {
            showJobsList();
            adapter.updateJobs(filtered);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView(); // Refresh the job list when returning to this activity
    }
}
