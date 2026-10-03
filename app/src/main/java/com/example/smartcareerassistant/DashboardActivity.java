package com.example.smartcareerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        // Resume Builder
        findViewById(R.id.cardResume).setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    ResumeBuilderActivity.class
            );
            startActivity(intent);
        });

        // Mock Interview
        findViewById(R.id.cardInterview).setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    MockInterviewActivity.class
            );
            startActivity(intent);
        });

        // AI Career Assistant
        findViewById(R.id.cardAI).setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    AIAssistantActivity.class
            );
            startActivity(intent);
        });

        // Placement Preparation
        findViewById(R.id.cardPlacement).setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    PlacementPreparationActivity.class
            );
            startActivity(intent);
        });

        // Logout
        findViewById(R.id.btnLogout).setOnClickListener(v -> {

            mAuth.signOut();

            Toast.makeText(
                    DashboardActivity.this,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });
    }
}