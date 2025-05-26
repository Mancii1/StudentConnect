package com.example.student_recuitment_app.Application;
public class JobApplication {
    private int id;
    private int jobId;

    private String candidateName;
    private String candidateEmail;
    private String jobTitle;
    private String status;

    public JobApplication(int applicationId, int jobId, String status,
                          String jobTitle, String studentName, String studentEmail) {
        this.id = applicationId;
        this.jobId = jobId;
        this.candidateName = studentName;
        this.candidateEmail = studentEmail;
        this.jobTitle = jobTitle;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getStatus() { return status; }
    public int getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }


    public void setStatus(String status) {
        this.status = status;
    }
}