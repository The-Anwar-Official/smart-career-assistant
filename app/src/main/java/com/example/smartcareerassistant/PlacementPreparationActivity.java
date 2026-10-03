package com.example.smartcareerassistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlacementPreparationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_placement_preparation);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );

        // Aptitude
        findViewById(R.id.cardAptitude).setOnClickListener(v -> {
            Intent intent = new Intent(
                    PlacementPreparationActivity.this,
                    AptitudeActivity.class
            );
            startActivity(intent);
        });

        // Technical Skills
        findViewById(R.id.cardTechnical).setOnClickListener(v -> {
            Intent intent = new Intent(
                    PlacementPreparationActivity.this,
                    TechnicalSkillsActivity.class
            );
            startActivity(intent);
        });

        // DSA Practice
        findViewById(R.id.cardDSA).setOnClickListener(v -> {
            Intent intent = new Intent(
                    PlacementPreparationActivity.this,
                    DSAPracticeActivity.class
            );
            startActivity(intent);
        });

        // HR Interview
        findViewById(R.id.cardHR).setOnClickListener(v -> {
            Intent intent = new Intent(
                    PlacementPreparationActivity.this,
                    HRInterviewActivity.class
            );
            startActivity(intent);
        });
    }
}