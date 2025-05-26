package com.example.student_recuitment_app.authantication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.student_recuitment_app.Admin.activity_admin_dashboard;
import com.example.student_recuitment_app.DatabaseHelper;
import com.example.student_recuitment_app.Employer.HomeEmployerActivity;
import com.example.student_recuitment_app.Graduate.Profile.HomegraduateDashboardActivity;
import com.example.student_recuitment_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class loginActivity extends AppCompatActivity {
    TextInputEditText etEmail, etPassword;
    MaterialButton btnLogin;
    TextView btnRegisterCandidate, tvForgotPassword;
    ChipGroup roleGroup;
    Chip selectedRoleChip;
    DatabaseHelper dbHelper;
    CardView loginCard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterCandidate = findViewById(R.id.btnRegisterCandidate);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        roleGroup = findViewById(R.id.roleGroup1);
        loginCard = findViewById(R.id.loginCard);

        dbHelper = new DatabaseHelper(this);

        // Add entrance animation
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        loginCard.startAnimation(slideUp);

        // Add button press animation
        Animation buttonPress = AnimationUtils.loadAnimation(this, R.anim.button_press);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("admin@example.com")) {
                    roleGroup.setVisibility(View.GONE);
                    Animation fadeOut = AnimationUtils.loadAnimation(loginActivity.this, R.anim.fade_out);
                    roleGroup.startAnimation(fadeOut);
                } else {
                    roleGroup.setVisibility(View.VISIBLE);
                    Animation fadeIn = AnimationUtils.loadAnimation(loginActivity.this, R.anim.fade_in);
                    roleGroup.startAnimation(fadeIn);
                }
            }
        });

        btnLogin.setOnClickListener(v -> {
            // Add button press animation
            v.startAnimation(buttonPress);

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(loginActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check Admin FIRST and exit immediately if admin
            if (dbHelper.isAdminValid(email, password)) {
                startActivity(new Intent(loginActivity.this, activity_admin_dashboard.class));
                finish();
                return;
            }

            // Only if NOT Admin, proceed with role selection
            int selectedRoleId = roleGroup.getCheckedChipId();
            if (selectedRoleId == View.NO_ID) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedRoleChip = findViewById(selectedRoleId);
            if (selectedRoleChip == null) {
                Toast.makeText(this, "Invalid role selected", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedRole = selectedRoleChip.getText().toString().trim();

            String actualRole = dbHelper.checkLogin(email, password);
            if ("invalid".equals(actualRole)) {
                Toast.makeText(loginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedRole.equalsIgnoreCase("candidate") && "candidate".equals(actualRole)) {
                Intent intent = new Intent(loginActivity.this, HomegraduateDashboardActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else if (selectedRole.equalsIgnoreCase("employer") && "employer".equals(actualRole)) {
                int employerId = dbHelper.getEmployerIdByEmail(email);

                if(employerId != -1) {
                    Intent intent = new Intent(loginActivity.this, HomeEmployerActivity.class);
                    intent.putExtra("employer_id", employerId);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Employer not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegisterCandidate.setOnClickListener(v -> {
            v.startAnimation(buttonPress);
            startActivity(new Intent(loginActivity.this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            v.startAnimation(buttonPress);
            // Add your forgot password logic here
            Toast.makeText(this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}