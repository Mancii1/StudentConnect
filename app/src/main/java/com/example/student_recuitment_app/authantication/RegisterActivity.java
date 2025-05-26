package com.example.student_recuitment_app.authantication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etName, etEmail, etPassword, etConfirm, etCompanyName;
    TextInputLayout tilCompanyName;
    ChipGroup radioUserType;
    MaterialButton btnRegister;
    TextView btnBackToLogin;
    CardView registerCard;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        radioUserType = findViewById(R.id.radioUserType);
        etName = findViewById(R.id.etCandidateName);
        etEmail = findViewById(R.id.etCandidateEmail);
        etPassword = findViewById(R.id.etCandidatePassword);
        etConfirm = findViewById(R.id.etCandidateConfirmPassword);
        etCompanyName = findViewById(R.id.etCompanyName);
        tilCompanyName = findViewById(R.id.tilCompanyName);
        btnRegister = findViewById(R.id.btnCandidateRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        registerCard = findViewById(R.id.registerCard);

        dbHelper = new DatabaseHelper(this);

        // Add entrance animation
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        registerCard.startAnimation(slideUp);

        // Add button press animation
        Animation buttonPress = AnimationUtils.loadAnimation(this, R.anim.button_press);

        // Handle user type selection
        radioUserType.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = findViewById(checkedId);
            if (selectedChip != null && selectedChip.getTag().toString().equals("employer")) {
                tilCompanyName.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                tilCompanyName.startAnimation(fadeIn);
            } else {
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tilCompanyName.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                tilCompanyName.startAnimation(fadeOut);
            }
        });

        btnRegister.setOnClickListener(v -> {
            // Add button press animation
            v.startAnimation(buttonPress);

            int selectedId = radioUserType.getCheckedChipId();
            Chip selectedChip = findViewById(selectedId);

            if (selectedId == View.NO_ID) {
                Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = selectedChip.getTag().toString();
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirm.getText().toString();

            if (validateInputs(userType, name, email, password, confirmPassword)) {
                handleRegistration(userType, name, email, password);
            }
        });

        btnBackToLogin.setOnClickListener(v -> {
            v.startAnimation(buttonPress);
            startActivity(new Intent(RegisterActivity.this, loginActivity.class));
            finish();
        });
    }

    private boolean validateInputs(String userType, String name, String email,
                                   String password, String confirmPassword) {
        // Common validations
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // User-specific validations
        if (userType.equals("graduate")) {
            if (!email.endsWith(".ac.za")) {
                Toast.makeText(this, "Graduate email must be a university email (.ac.za)",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (!password.matches("^\\$\\$Dut\\d{6}$")) { // Case-sensitive check
                Toast.makeText(this,
                        "Graduate password must follow: $$Dut + 6-digit ID (e.g., $$Dut123456)",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else if (userType.equals("employer")) {
            if (etCompanyName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Company name is required", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!isStrongPassword(password)) {
                Toast.makeText(this, "Password must contain: 8+ chars, uppercase, lowercase, number, special",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void handleRegistration(String userType, String name,
                                    String email, String password) {
        if (userType.equals("graduate")) {
            if (dbHelper.insertGraduate(name, email, password)) {
                Toast.makeText(this, "Graduate registration successful!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
            }
        } else {
            String companyName = etCompanyName.getText().toString().trim();
            if (dbHelper.insertEmployer(name, email, password, companyName)) {
                Toast.makeText(this, "Employer registration successful!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(this, "Password must contain at least one uppercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(this, "Password must contain at least one lowercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!password.matches(".*[@#$%^&+=!].*")) {
            Toast.makeText(this, "Password must contain at least one special character (@#$%^&+=!)", Toast.LENGTH_LONG).show();
            return false;
        }
        
        return true;
    }

    private void navigateToLogin() {
        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
        finish();
    }
}