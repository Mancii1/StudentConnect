package com.example.student_recuitment_app.Application;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.Application.JobApplication;
import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {
    private static final String TAG = "ApplicationsAdapter";
    private final List<JobApplication> applications;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public ApplicationsAdapter(List<JobApplication> applications, Context context) {
        this.applications = applications;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ApplicationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
            return new ViewHolder(view);
        } catch (Exception e) {
            Log.e(TAG, "Error creating view holder: " + e.getMessage());
            throw new RuntimeException("Error creating view holder", e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationsAdapter.ViewHolder holder, int position) {
        try {
            if (applications == null || position >= applications.size()) {
                Log.e(TAG, "Invalid position or null applications list");
                return;
            }

            JobApplication application = applications.get(position);
            if (application == null) {
                Log.e(TAG, "Application at position " + position + " is null");
                return;
            }

            // Set job title
            String jobTitle = application.getJobTitle();
            holder.tvJobTitle.setText(jobTitle != null ? jobTitle : "Unknown Job");

            // Set status
            String status = application.getStatus();
            holder.tvStatus.setText("Status: " + (status != null ? status : "Pending"));

            // Set status bar color based on application status
            int color;
            if (status != null) {
                switch (status.toLowerCase()) {
                    case "accepted":
                        color = Color.parseColor("#4CAF50"); // Green
                        break;
                    case "rejected":
                        color = Color.parseColor("#F44336"); // Red
                        break;
                    default:
                        color = Color.parseColor("#9E9E9E"); // Gray (Pending)
                }
            } else {
                color = Color.parseColor("#9E9E9E"); // Default gray
            }
            holder.statusBar.setBackgroundColor(color);

            // Handle delete button click
            holder.btnDelete.setOnClickListener(v -> {
                try {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Application")
                            .setMessage("Are you sure you want to delete this application?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                try {
                                    boolean isDeleted = dbHelper.deleteApplicationById(application.getId());
                                    if (isDeleted) {
                                        applications.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, applications.size());
                                        Toast.makeText(context, "Application deleted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to delete application", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error deleting application: " + e.getMessage());
                                    Toast.makeText(context, "Error deleting application", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } catch (Exception e) {
                    Log.e(TAG, "Error showing delete dialog: " + e.getMessage());
                    Toast.makeText(context, "Error showing delete dialog", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error binding view holder: " + e.getMessage());
            Toast.makeText(context, "Error displaying application", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return applications != null ? applications.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvStatus;
        View statusBar;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                statusBar = itemView.findViewById(R.id.statusBar);
                btnDelete = itemView.findViewById(R.id.btnDelete);

                if (tvJobTitle == null || tvStatus == null || statusBar == null || btnDelete == null) {
                    throw new IllegalStateException("Required views not found in layout");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error initializing ViewHolder: " + e.getMessage());
                throw new RuntimeException("Error initializing ViewHolder", e);
            }
        }
    }
}
