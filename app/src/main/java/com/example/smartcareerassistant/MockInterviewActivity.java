package com.example.smartcareerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MockInterviewActivity extends AppCompatActivity {

    private TextView tvQuestionNumber;
    private TextView tvProgress;
    private TextView tvQuestion;
    private ProgressBar progressBar;
    private EditText etAnswer;

    private int currentQuestion = 0;
    private int score = 0;

    private final String[] questions = {
            "Tell me about yourself.",
            "What are your strengths?",
            "Why should we hire you?",
            "Where do you see yourself in five years?",
            "Why do you want to join our company?"
    };

    private final String[][] keywords = {
            {"name", "study", "bca", "education", "skill", "experience"},
            {"strength", "hardworking", "team", "learn", "communication", "problem"},
            {"skill", "experience", "learn", "team", "company", "value"},
            {"career", "goal", "future", "experience", "learn", "growth"},
            {"company", "career", "growth", "learn", "skill", "opportunity"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mock_interview);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        progressBar = findViewById(R.id.progressBar);
        etAnswer = findViewById(R.id.etAnswer);

        findViewById(R.id.btnSubmit).setOnClickListener(v -> submitAnswer());

        showQuestion();
    }

    private void showQuestion() {

        tvQuestion.setText(questions[currentQuestion]);

        int questionNumber = currentQuestion + 1;
        int progress = (questionNumber * 100) / questions.length;

        tvQuestionNumber.setText(
                "Question " + questionNumber + " of " + questions.length
        );

        tvProgress.setText(progress + "%");

        progressBar.setProgress(progress);

        etAnswer.setText("");

        etAnswer.requestFocus();
    }

    private void submitAnswer() {

        String answer = etAnswer.getText().toString().trim().toLowerCase();

        if (answer.isEmpty()) {
            etAnswer.setError("Please enter your answer");
            return;
        }

        if (answer.length() < 10) {
            etAnswer.setError("Please write a little more");
            return;
        }

        int questionScore = calculateScore(answer);

        score += questionScore;

        if (currentQuestion < questions.length - 1) {

            Toast.makeText(
                    this,
                    "Answer submitted! +" + questionScore + " points",
                    Toast.LENGTH_SHORT
            ).show();

            currentQuestion++;
            showQuestion();

        } else {

            showFinalResult();
        }
    }

    private int calculateScore(String answer) {

        int matchedKeywords = 0;

        for (String keyword : keywords[currentQuestion]) {

            if (answer.contains(keyword)) {
                matchedKeywords++;
            }
        }

        if (matchedKeywords >= 3) {
            return 20;
        } else if (matchedKeywords == 2) {
            return 15;
        } else if (matchedKeywords == 1) {
            return 10;
        } else {
            return 5;
        }
    }

    private void showFinalResult() {

        String performance;

        if (score >= 80) {
            performance = "Excellent! You are interview-ready. 🎉";
        } else if (score >= 60) {
            performance = "Good performance! Keep practicing. 👍";
        } else if (score >= 40) {
            performance = "Fair performance. More practice will help. 💪";
        } else {
            performance = "Keep practicing and improve your answers. 📚";
        }

        new AlertDialog.Builder(this)
                .setTitle("Interview Complete 🎤")
                .setMessage(
                        "Your Score: " + score + " / 100\n\n" +
                                performance
                )
                .setCancelable(false)
                .setPositiveButton("Try Again", (dialog, which) -> {

                    currentQuestion = 0;
                    score = 0;
                    showQuestion();

                })
                .setNegativeButton("Dashboard", (dialog, which) -> {

                    Intent intent = new Intent(
                            MockInterviewActivity.this,
                            DashboardActivity.class
                    );

                    startActivity(intent);
                    finish();
                })
                .show();
    }
}