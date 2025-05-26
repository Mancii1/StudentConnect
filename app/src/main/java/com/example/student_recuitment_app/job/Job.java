package com.example.student_recuitment_app.job;

public class Job {
    private int id;
    private String title;
    private String type;
    private String description;
    private String salary;
    private String location;
    private String employer_id;

    public Job(int id, String title,String type, String description, String salary, String location) {
        this.id = id;
        this.type = type;
        this.type = type;
        this.description = description;
        this.salary = salary;
        this.location = location;
        this.employer_id = employer_id;
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public String getSalary() {
        return salary;
    }
    public String getLocation() {
        return location;
        }
    public String getEmployer_id() {
        return employer_id;
    }


}
