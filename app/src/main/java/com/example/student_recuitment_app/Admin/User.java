package com.example.student_recuitment_app.Admin;

public class User {
    private String name;
    private String email;
    private String role; // "Graduate" or "Employer"
    private String details; // Company (employer) or Degree (graduate)

    public User(String name, String email, String role, String details) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.details = details;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getDetails() { return details; }
}
