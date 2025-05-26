package com.example.student_recuitment_app.Employer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

public class post_job extends AppCompatActivity {
    private int employerId; // Add this
     EditText editTextJobTitle, editTextJobType, editTextJobDescription, editTextJobSalary, editTextJobLocation;
    Button buttonPostJob;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        employerId = getIntent().getIntExtra("employer_id", -1);
        if(employerId == -1) {
            Toast.makeText(this, "Employer not recognized", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextJobTitle = findViewById(R.id.editTextJobTitle);
        editTextJobType = findViewById(R.id.editTextJobType);
        editTextJobDescription = findViewById(R.id.editTextJobDescription);
        editTextJobSalary = findViewById(R.id.editTextJobSalary);
        editTextJobLocation = findViewById(R.id.editTextJobLocation);

        buttonPostJob = findViewById(R.id.buttonPostJob);

        databaseHelper = new DatabaseHelper(this);

        buttonPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTextJobTitle.getText().toString().trim();
                String type = editTextJobType.getText().toString().trim();
                String description = editTextJobDescription.getText().toString().trim();
                String salary = editTextJobSalary.getText().toString().trim();
                String location = editTextJobLocation.getText().toString().trim();
                int employer_id = employerId;


                if (title.isEmpty() || type.isEmpty() || description.isEmpty() || salary.isEmpty() || location.isEmpty()) {
                    Toast.makeText(post_job.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.insertJob( title, type, description, salary, location,employer_id);

                    Toast.makeText(post_job.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();

                    // Clear fields
                    editTextJobTitle.setText("");
                    editTextJobType.setText("");
                    editTextJobDescription.setText("");
                    editTextJobSalary.setText("");
                    editTextJobLocation.setText("");
                }
            }
        });
    }
}