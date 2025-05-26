package com.example.student_recuitment_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.student_recuitment_app.Admin.User;
import com.example.student_recuitment_app.Application.JobApplication;
import com.example.student_recuitment_app.Graduate.Profile.Graduate;
import com.example.student_recuitment_app.job.Job;

import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_recruitment_app.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_APPLICATIONS = "applications";
    private static final String KEY_APP_ID = "id";
    private static final String KEY_JOB_ID = "job_id";
    private static final String KEY_GRADUATE_ID = "graduate_id";
    private static final String KEY_STATUS = "status";

    private static final String KEY_TITLE = "title";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String graduateTable = "CREATE TABLE graduates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "grad_loc TEXT," +
                "grad_degree TEXT," +
                "grad_file BLOB," +
                "grad_skills TEXT," +
                "grad_bio TEXT," +
                "grad_image BLOB)";

        String employerTable = "CREATE TABLE employer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL," +  // <-- added password
                "company_name TEXT NOT NULL)";

        String jobTable = "CREATE TABLE job (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "description TEXT," +
                "salary TEXT NOT NULL," +
                "location_JOB TEXT NOT NULL,"+
                "employer_id INTEGER NOT NULL)";

        String applicationsTable = "CREATE TABLE " + TABLE_APPLICATIONS + "("
                + KEY_APP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_JOB_ID + " INTEGER NOT NULL,"
                + KEY_GRADUATE_ID + " INTEGER NOT NULL,"
                + KEY_STATUS + " TEXT DEFAULT 'Pending',"
                + "FOREIGN KEY(" + KEY_JOB_ID + ") REFERENCES job(id),"
                + "FOREIGN KEY(" + KEY_GRADUATE_ID + ") REFERENCES graduates(id))";

        String adminTable = "CREATE TABLE admin (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL)";


        db.execSQL(graduateTable);
        db.execSQL(employerTable);
        db.execSQL(applicationsTable);
        db.execSQL(jobTable);
        db.execSQL(adminTable);

        // Example: Insert a default admin (run once)

        ContentValues adminValues = new ContentValues();
        adminValues.put("name", "Admin"); // <-- ADD THIS LINE
        adminValues.put("email", "admin@example.com");
        adminValues.put("password", "admin123");
        db.insert("admin", null, adminValues);


        
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

        db.execSQL("DROP TABLE IF EXISTS graduates");
        db.execSQL("DROP TABLE IF EXISTS employer");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATIONS);
        db.execSQL("DROP TABLE IF EXISTS job");

        db.execSQL("DROP TABLE IF EXISTS admin");
        onCreate(db); // recreate tables


        }


    }

    public boolean isAdminValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM admin WHERE email=? AND password=?",
                new String[]{email, password}
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
    // Insert Graduate
    public boolean insertGraduate(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("graduates", null, values);
        db.close();
        return result != -1;


    }

    // Update Graduate Profile
    // In DatabaseHelper.java
    public void updateGraduateProfile(int id, String location, String degree,
                                      byte[] cv, byte[] image, String skills, String bio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (location != null) values.put("grad_loc", location);
        if (degree != null) values.put("grad_degree", degree);
        if (cv != null) values.put("grad_file", cv);
        if (image != null) values.put("grad_image", image);
        if (skills != null) values.put("grad_skills", skills);
        if (bio != null) values.put("grad_bio", bio);

        db.update("graduates", values, "id=?", new String[]{String.valueOf(id)});
//        db.close(); // Ensure proper closing
    }



    // Insert Employer
    public boolean insertEmployer(String name, String email, String password, String companyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("company_name", companyName);

        long re = db.insert("employer", null, values);
        db.close();
        return re != -1;

    }

    // Check Login
    // In DatabaseHelper.java
    public String checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check graduates
        Cursor gradCursor = db.rawQuery(
                "SELECT * FROM graduates WHERE email=? AND password=?",
                new String[]{email, password}
        );
        if (gradCursor.getCount() > 0) {
            gradCursor.close();
            return "candidate";
        }

        // Check employers
        Cursor empCursor = db.rawQuery(
                "SELECT * FROM employer WHERE email=? AND password=?",
                new String[]{email, password}
        );
        if (empCursor.getCount() > 0) {
            empCursor.close();
            return "employer";
        }

        return "invalid";
    }

    // Check if email is unique
    public boolean isEmailUnique(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM graduates WHERE email=?",
                new String[]{email}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return !exists;
    }


    // Get Graduate by ID
    public Graduate  getGraduateById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("graduates", null, "id=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Graduate graduate = new Graduate(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("grad_loc")),
                    cursor.getString(cursor.getColumnIndexOrThrow("grad_degree")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("grad_file")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("grad_image")),
                    cursor.getString(cursor.getColumnIndexOrThrow("grad_skills")),
                    cursor.getString(cursor.getColumnIndexOrThrow("grad_bio"))
                    );

            cursor.close();
            return graduate;
        }
        return null;
    }


    // Get Graduate ID by Email
// In DatabaseHelper.java
    public int getGraduateIdByEmail(String email) {
        if (email == null) return -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM graduates WHERE email=?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public void insertJob(String title, String type, String description, String salary, String location , int employer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("type", type);
        values.put("description", description);
        values.put("salary", salary);
        values.put("location_JOB", location);
        values.put("employer_id", employer_id);
        db.insert("job", null, values);
        db.close();
    }

    // Get All Jobs
    // DatabaseHelper.java
    public List<Job> getAllJobs() {
        List<Job> jobList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM job", null);

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("salary")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location_JOB"))
                );
                jobList.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jobList;
    }

    public Job getJobById(int jobId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM job WHERE id = ?", new String[]{String.valueOf(jobId)});
        if (cursor.moveToFirst()) {
            Job job = new Job(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("salary")),
                    cursor.getString(cursor.getColumnIndexOrThrow("location_JOB"))
            );
            cursor.close();
            return job;
        }
        cursor.close();
        return null;
    }

    public void applyToJob(int graduateId, int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("graduate_id", graduateId);
        values.put("job_id", jobId);
        db.insert("applications", null, values);
    }

    // DatabaseHelper.java
    public boolean applyForJob(int jobId, String studentEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        int graduateId = getGraduateIdByEmail(studentEmail);

        if(graduateId == -1) return false;

        ContentValues values = new ContentValues();
        values.put(KEY_JOB_ID, jobId);
        values.put(KEY_GRADUATE_ID, graduateId);

        long result = db.insert(TABLE_APPLICATIONS, null, values);
        db.close();
        return result != -1;
    }

    public List<JobApplication> getApplicationsByStudent(String email) {
        List<JobApplication> applications = new ArrayList<>();
        int graduateId = getGraduateIdByEmail(email);
        if(graduateId == -1) return applications;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.id AS application_id, " +
                "a.job_id, " +
                "a.status, " +
                "j.title AS job_title, " +  // Use title instead of type
                "g.name, " +
                "g.email " +
                "FROM applications a " +
                "INNER JOIN job j ON a.job_id = j.id " +
                "INNER JOIN graduates g ON a.graduate_id = g.id " +
                "WHERE a.graduate_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(graduateId)});

        if (cursor.moveToFirst()) {
            do {
                JobApplication app = new JobApplication(
                        cursor.getInt(cursor.getColumnIndexOrThrow("application_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("job_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("job_title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email"))

                );
                applications.add(app);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return applications;
    }

    public boolean hasApplied(int jobId, String studentEmail) {
        if (studentEmail == null) return false;

        int graduateId = getGraduateIdByEmail(studentEmail);
        if (graduateId == -1) return false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_APPLICATIONS + " WHERE "
                + KEY_JOB_ID + " = ? AND " + KEY_GRADUATE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(jobId), String.valueOf(graduateId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Add to DatabaseHelper.java
    public List<JobApplication> getApplicationsForEmployer(int employerId) {
        List<JobApplication> applications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query =  "SELECT " +
                "a.id AS application_id, " +
                "a.job_id, " +
                "a.status, " +
                "g.name AS candidate_name, " +
                "g.email AS candidate_email, " +
                "j.title AS job_title " +
                "FROM applications a " + // Proper alias definition
                "INNER JOIN job j ON a.job_id = j.id " +
                "INNER JOIN graduates g ON a.graduate_id = g.id " +
                "WHERE j.employer_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employerId)});

        if (cursor.moveToFirst()) {
            do {
                JobApplication app = new JobApplication(
                        cursor.getInt(cursor.getColumnIndexOrThrow("application_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("job_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("candidate_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("candidate_email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("job_title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                );
                applications.add(app);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return applications;
    }

    public boolean updateApplicationStatus(int applicationId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        int result = db.update("applications", values, "id=?",
                new String[]{String.valueOf(applicationId)});
        return result > 0;
    }


    // Get All Graduates (Example for listing)
    public List<Graduate> getAllGraduates() {
        List<Graduate> graduateList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, email, password, grad_loc, grad_degree, grad_file, grad_image, grad_skills, grad_bio FROM graduates", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String password = cursor.getString(3);
                String grad_loc = cursor.getString(4);
                String grad_degree = cursor.getString(5);
                byte[] grad_file = cursor.getBlob(6);
                byte[] grad_image = cursor.getBlob(7);
                String grad_skills = cursor.getString(8);
                String grad_bio = cursor.getString(9);

                Graduate graduate = new Graduate(id, name, email, password, grad_loc, grad_degree, grad_file, grad_image, grad_skills, grad_bio);
                graduateList.add(graduate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return graduateList;
    }


    // Fetch all graduates
    public List<User> getALGraduates() {
        List<User> graduates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT name, email, grad_degree FROM graduates",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                graduates.add(new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        "Graduate",
                        "Degree: " + cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return graduates;
    }

    // Fetch all employers
    public List<User> getAllEmployers() {
        List<User> employers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT name, email, company_name FROM employer",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                employers.add(new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        "Employer",
                        "Company: " + cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return employers;
    }

    // Get Employer ID by Email
    public int getEmployerIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM employer WHERE email = ?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public boolean deleteUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("graduates", "email=?", new String[]{email});
        return result > 0;
    }

    public boolean deleteApplicationById(int applicationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("applications", "id = ?", new String[]{String.valueOf(applicationId)});
        db.close();
        return result > 0;
    }



















}




