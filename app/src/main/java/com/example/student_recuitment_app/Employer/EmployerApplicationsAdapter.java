package com.example.student_recuitment_app.Employer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_recuitment_app.Application.JobApplication;
import com.example.student_recuitment_app.R;

import java.util.List;

public class EmployerApplicationsAdapter extends RecyclerView.Adapter<EmployerApplicationsAdapter.ViewHolder> {
    private List<JobApplication> applications;
    private final OnStatusChangeListener statusListener;

    public interface OnStatusChangeListener {
        void onStatusChanged(int applicationId, String newStatus);
    }

    public EmployerApplicationsAdapter(List<JobApplication> applications, OnStatusChangeListener listener) {
        this.applications = applications;
        this.statusListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employer_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobApplication application = applications.get(position);
        holder.bind(application, statusListener);
    }

    @Override
    public int getItemCount() {
        // FIX 1: Return actual list size instead of 0
        return applications != null ? applications.size() : 0;
    }

    public void updateData(List<JobApplication> newData) {
        // FIX 2: Proper list update implementation
        applications.clear();
        applications.addAll(newData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCandidateName, tvCandidateEmail, tvJobTitle, tvStatus;
        Button btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCandidateName = itemView.findViewById(R.id.tvCandidateName);
            tvCandidateEmail = itemView.findViewById(R.id.tvCandidateEmail);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }

        public void bind(JobApplication application, OnStatusChangeListener listener) {
            // FIX 3: Add null checks and proper data binding
            tvCandidateName.setText(application.getCandidateName() != null ?
                    application.getCandidateName() : "Unknown Candidate");

            tvCandidateEmail.setText(application.getCandidateEmail() != null ?
                    application.getCandidateEmail() : "No email provided");

            tvJobTitle.setText(application.getJobTitle() != null ?
                    application.getJobTitle() : "Untitled Position");

            // FIX 4: Add status validation
            String status = application.getStatus() != null ?
                    application.getStatus() : "Pending";
            tvStatus.setText(String.format("Status: %s", status));

            // FIX 5: Handle button states based on current status
            updateButtonVisibility(status);

            btnAccept.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStatusChanged(application.getId(), "Accepted");
                    // Immediately update UI
                    application.setStatus("Accepted");
                    notifyItemChanged(getAdapterPosition());
                }
            });

            btnReject.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStatusChanged(application.getId(), "Rejected");
                    updateButtonVisibility("Rejected");
                }
            });
        }

        private void updateButtonVisibility(String status) {
            switch (status) {
                case "Accepted":
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.VISIBLE);
                    break;
                case "Rejected":
                    btnAccept.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.GONE);
                    break;
                default:
                    btnAccept.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
            }
            tvStatus.setText(String.format("Status: %s", status));
        }
    }
}