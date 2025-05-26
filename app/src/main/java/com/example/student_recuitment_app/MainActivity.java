package com.example.student_recuitment_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.student_recuitment_app.authantication.RegisterActivity;
import com.example.student_recuitment_app.authantication.loginActivity;
import com.example.student_recuitment_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MaterialButton buttonlogin;
    private MaterialButton buttonreg;
    private MaterialCardView welcomeCard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();

        // Apply animations
        applyAnimations();

        // Setup button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        textView = findViewById(R.id.textView);
        buttonlogin = findViewById(R.id.buttonlogin);
        buttonreg = findViewById(R.id.buttonreg);
        welcomeCard = findViewById(R.id.welcomeCard);
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

        // Apply animations to cards and buttons
        welcomeCard.startAnimation(fadeIn);
        buttonlogin.startAnimation(slideUp);
        buttonreg.startAnimation(slideUp);

        // Apply button press animation to all buttons
        View[] buttons = {buttonlogin, buttonreg};
        for (View button : buttons) {
            button.setOnTouchListener((v, event) -> {
                Animation buttonPress = AnimationUtils.loadAnimation(this, R.anim.button_press);
                v.startAnimation(buttonPress);
                return false;
            });
        }
    }

    private void setupButtonListeners() {
        buttonlogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        buttonreg.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}