package com.example.student_recuitment_app.job;
// JobAdapter.java


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;
    private final OnItemClickListener listener;
    private final String loggedInEmail;
    private final DatabaseHelper dbHelper;

    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    public JobAdapter(List<Job> jobList, OnItemClickListener listener,
                      String loggedInEmail, DatabaseHelper dbHelper) {
        this.jobList = jobList;
        this.listener = listener;
        this.loggedInEmail = loggedInEmail;
        this.dbHelper = dbHelper;
    }

    public void updateJobs(List<Job> newJobs) {
        this.jobList = newJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobDescription, tvSalary, tvLocation;
        Button btnApply;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobDescription = itemView.findViewById(R.id.tvJobDescription);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnApply = itemView.findViewById(R.id.btnApply);
        }

        void bind(Job job) {
            tvJobTitle.setText(job.getTitle());
            tvJobDescription.setText(job.getDescription());
            tvSalary.setText("Salary: " + job.getSalary());
            tvLocation.setText("Location: " + job.getLocation());

            boolean hasApplied = (loggedInEmail != null) &&
                    dbHelper.hasApplied(job.getId(), loggedInEmail);

            btnApply.setEnabled(!hasApplied);
            btnApply.setText(hasApplied ? "Applied" : "Apply");

            btnApply.setOnClickListener(v -> {
                if (!hasApplied) {
                    boolean success = dbHelper.applyForJob(job.getId(), loggedInEmail);
                    if (success) {
                        btnApply.setEnabled(false);
                        btnApply.setText("Applied");
                        Toast.makeText(itemView.getContext(),
                                "Application submitted!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(v -> listener.onItemClick(job));
        }
    }
}
