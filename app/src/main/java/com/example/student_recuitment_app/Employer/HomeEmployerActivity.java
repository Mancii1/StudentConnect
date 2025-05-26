package com.example.student_recuitment_app.Employer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.student_recuitment_app.MainActivity;
import com.example.student_recuitment_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class HomeEmployerActivity extends AppCompatActivity {
    private MaterialButton btnManageCandidates, btnViewApplications, btnPostJob, btnLogout;
    private MaterialCardView welcomeCard, actionsCard;
    private TextView tvWelcome;
    private int employerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employer);

        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();

        // Get employer ID
        employerId = getIntent().getIntExtra("employer_id", -1);
        if(employerId == -1) {
            Toast.makeText(this, "Employer not recognized", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Apply animations
        applyAnimations();

        // Setup button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        btnManageCandidates = findViewById(R.id.btnManageCandidates);
        btnViewApplications = findViewById(R.id.btnViewApplications);
        btnPostJob = findViewById(R.id.btnPostJob);
        btnLogout = findViewById(R.id.btnLogoutEmployer);
        welcomeCard = findViewById(R.id.welcomeCard);
        actionsCard = findViewById(R.id.actionsCard);
        tvWelcome = findViewById(R.id.tvWelcome);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void applyAnimations() {
        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply animations to cards
        welcomeCard.startAnimation(fadeIn);
        actionsCard.startAnimation(slideUp);

        // Apply button press animation to all buttons
        View[] buttons = {btnManageCandidates, btnViewApplications, btnPostJob, btnLogout};
        for (View button : buttons) {
            button.setOnTouchListener((v, event) -> {
                Animation buttonPress = AnimationUtils.loadAnimation(this, R.anim.button_press);
                v.startAnimation(buttonPress);
                return false;
            });
        }
    }

    private void setupButtonListeners() {
        btnPostJob.setOnClickListener(v -> {
            Intent intent = new Intent(this, post_job.class);
            intent.putExtra("employer_id", employerId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnViewApplications.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmployerApplicationsActivity.class);
            intent.putExtra("employer_id", employerId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnManageCandidates.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewGraduatesActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}