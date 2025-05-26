package com.example.student_recuitment_app.Application;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ApplicationsListActivity extends AppCompatActivity {
    private static final String TAG = "ApplicationsListActivity";
    private RecyclerView recyclerView;
    private ApplicationsAdapter adapter;
    private DatabaseHelper dbHelper;
    private String loggedInEmail;
    private MaterialCardView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "Starting onCreate");
            setContentView(R.layout.activity_applications_list);

            // Initialize views
            recyclerView = findViewById(R.id.applicationsRecyclerView);
            emptyView = findViewById(R.id.emptyView);

            if (recyclerView == null || emptyView == null) {
                Log.e(TAG, "Required views not found in layout");
                Toast.makeText(this, "Error initializing views", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Get logged-in email
            loggedInEmail = getIntent().getStringExtra("email");
            Log.d(TAG, "Logged in email: " + (loggedInEmail != null ? loggedInEmail : "null"));
            
            if (loggedInEmail == null) {
                Log.e(TAG, "No email provided in intent");
                Toast.makeText(this, "Error: User session not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            dbHelper = new DatabaseHelper(this);
            setupRecyclerView();
            Log.d(TAG, "onCreate completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing activity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        try {
            Log.d(TAG, "Setting up RecyclerView");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Load applications
            List<JobApplication> applications = dbHelper.getApplicationsByStudent(loggedInEmail);
            Log.d(TAG, "Retrieved applications: " + (applications != null ? applications.size() : "null"));

            if (applications == null) {
                Log.e(TAG, "Database returned null applications list");
                showEmptyState();
                return;
            }

            if (applications.isEmpty()) {
                Log.d(TAG, "No applications found for user: " + loggedInEmail);
                showEmptyState();
                return;
            }

            adapter = new ApplicationsAdapter(applications, this);
            recyclerView.setAdapter(adapter);
            showApplicationsList();
            Log.d(TAG, "RecyclerView setup completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading applications: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading applications: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            showEmptyState();
        }
    }

    private void showEmptyState() {
        try {
            Log.d(TAG, "Showing empty state");
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "Error showing empty state: " + e.getMessage(), e);
        }
    }

    private void showApplicationsList() {
        try {
            Log.d(TAG, "Showing applications list");
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Error showing applications list: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d(TAG, "onResume called");
            setupRecyclerView(); // Refresh the applications list when returning to this activity
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing applications: " + e.getMessage(), e);
            Toast.makeText(this, "Error refreshing applications", Toast.LENGTH_SHORT).show();
        }
    }
}