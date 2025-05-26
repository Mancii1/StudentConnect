package com.example.student_recuitment_app.Graduate.Profile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    EditText etLocation, etDegree, etSkills, etBio;
    Button btnUploadCV, btnUploadImage, btnSaveProfile;
    Button btnViewCV;
    TextView tvCVFileName;
    ImageView ivProfile;
    Uri cvUri = null, imageUri = null;

    DatabaseHelper dbHelper;
    String loggedInEmail = ""; // This should be passed via intent or session

    private static final int PICK_PDF_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etLocation = findViewById(R.id.etLocation);
        etDegree = findViewById(R.id.etDegree);
        etSkills = findViewById(R.id.etSkills);
        etBio = findViewById(R.id.etBio);
        btnUploadCV = findViewById(R.id.btnUploadCV);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        ivProfile = findViewById(R.id.ivProfile);
        tvCVFileName = findViewById(R.id.tvCVFileName);

        dbHelper = new DatabaseHelper(this);

        // Update the email retrieval with null check
        loggedInEmail = getIntent().getStringExtra("email");
        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            Toast.makeText(this, "No email provided", Toast.LENGTH_SHORT).show();
            finish(); // or handle as needed
        }
        else {
            loadUserProfile(loggedInEmail); // 🔁 Load saved profile
        }

        btnUploadCV.setOnClickListener(v -> choosePDF());
        btnUploadImage.setOnClickListener(v -> chooseImage());

        btnSaveProfile.setOnClickListener(v -> saveProfile());

        btnViewCV = findViewById(R.id.btnViewCV);

        btnViewCV.setOnClickListener(v -> {
            if (cvUri != null) {
                viewPDF(cvUri);
            } else {
                Toast.makeText(this, "No CV uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void viewPDF(Uri pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No PDF viewer found on your device", Toast.LENGTH_SHORT).show();
        }
    }


    private void choosePDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    imageUri = uri;
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        ivProfile.setImageBitmap(bitmap);
                    }
                    inputStream.close();
                } catch (Exception e) {
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_PDF_REQUEST) {
                cvUri = uri;
                String fileName = getFileName(uri);
                if (fileName != null && !fileName.isEmpty()) {
                    tvCVFileName.setText(fileName);  // ✅ Show the selected file name
                } else {
                    tvCVFileName.setText("No file selected");
                }
            }
        }
    }

    // Modified saveProfile() method
    private void saveProfile() {
        try {
            int id = getGraduateIdByEmail(loggedInEmail);
            if (id == -1) {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] cvBytes = null;
            byte[] imageBytes = null;

            // CV File Handling
            if (cvUri != null) {
                try (InputStream is = getContentResolver().openInputStream(cvUri);
                     ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    cvBytes = buffer.toByteArray();
                }
            }

            // Handle Image file
            if (imageUri != null) {
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // Reduce resolution by half
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                        imageBytes = baos.toByteArray();
                    }
                }
            }

            dbHelper.updateGraduateProfile(
                    id,
                    etLocation.getText().toString().trim(),
                    etDegree.getText().toString().trim(),
                    cvBytes,
                    imageBytes,
                    etSkills.getText().toString().trim(),
                    etBio.getText().toString().trim()
            );

            Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadUserProfile(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM graduates WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            etLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow("grad_loc")));
            etDegree.setText(cursor.getString(cursor.getColumnIndexOrThrow("grad_degree")));
            etSkills.setText(cursor.getString(cursor.getColumnIndexOrThrow("grad_skills")));
            etBio.setText(cursor.getString(cursor.getColumnIndexOrThrow("grad_bio")));


            // Set profile image
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("grad_image"));
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivProfile.setImageBitmap(bitmap);
            }
            // You can set CV filename display too if needed

        }
        cursor.close();
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String fileName = "";
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }

    private int getGraduateIdByEmail(String email) {
        if (email == null || email.isEmpty()) return -1;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT id FROM graduates WHERE email=?",
                    new String[]{email} // Pass email as String array
            );
            return (cursor != null && cursor.moveToFirst()) ?
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")) : -1;
        } finally {
            if (cursor != null) cursor.close();
            // Do NOT close the database connection here
        }
    }

}
