package com.example.smartcareerassistant;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class HRInterviewActivity extends AppCompatActivity {

    private Spinner spinnerRole;
    private Spinner spinnerType;
    private Spinner spinnerDifficulty;

    private MaterialButton btnStartInterview;
    private MaterialButton btnSubmitAnswer;

    private MaterialCardView cardSetup;

    private LinearLayout layoutInterview;

    private TextView tvQuestionNumber;
    private TextView tvInterviewStatus;
    private TextView tvInterviewer;
    private TextView tvQuestion;

    private TextInputEditText etAnswer;

    private int currentQuestion = 0;
    private int score = 0;

    private final String[] questions = {
            "Tell me about yourself.",

            "Why do you want to join our company?",

            "What are your strengths and how do they help you at work?",

            "Tell me about a project you have worked on.",

            "Where do you see yourself in the next three years?",

            "Why should we hire you?"
    };

    private final String[] modelKeywords = {
            "bca,student,skills,project,career",
            "company,role,growth,learn,contribute",
            "strength,communication,problem,team,learning",
            "project,technology,role,challenge,result",
            "career,skills,growth,experience,developer",
            "skills,project,learn,team,contribute"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_interview);

        initializeViews();
        setupSpinners();

        btnStartInterview.setOnClickListener(v -> startInterview());

        btnSubmitAnswer.setOnClickListener(v -> submitAnswer());
    }

    private void initializeViews() {

        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        btnStartInterview = findViewById(R.id.btnStartInterview);
        btnSubmitAnswer = findViewById(R.id.btnSubmitAnswer);

        cardSetup = findViewById(R.id.cardSetup);
        layoutInterview = findViewById(R.id.layoutInterview);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvInterviewStatus = findViewById(R.id.tvInterviewStatus);
        tvInterviewer = findViewById(R.id.tvInterviewer);
        tvQuestion = findViewById(R.id.tvQuestion);

        etAnswer = findViewById(R.id.etAnswer);
    }

    private void setupSpinners() {

        String[] roles = {
                "Software Developer",
                "Android Developer",
                "Web Developer",
                "Data Analyst"
        };

        String[] interviewTypes = {
                "HR / Behavioral",
                "General Placement",
                "Mixed Interview"
        };

        String[] difficulties = {
                "Beginner",
                "Intermediate",
                "Advanced"
        };

        setSpinnerData(spinnerRole, roles);
        setSpinnerData(spinnerType, interviewTypes);
        setSpinnerData(spinnerDifficulty, difficulties);
    }

    private void setSpinnerData(Spinner spinner, String[] data) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                data
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
    }

    private void startInterview() {

        currentQuestion = 0;
        score = 0;

        String selectedRole =
                spinnerRole.getSelectedItem().toString();

        cardSetup.setVisibility(View.GONE);
        layoutInterview.setVisibility(View.VISIBLE);

        tvInterviewer.setText(
                "HR Interviewer • " + selectedRole
        );

        tvInterviewStatus.setText(
                "INTERVIEW IN PROGRESS"
        );

        showQuestion();
    }

    private void showQuestion() {

        tvQuestionNumber.setText(
                "Question " +
                        (currentQuestion + 1) +
                        " of " +
                        questions.length
        );

        tvQuestion.setText(
                questions[currentQuestion]
        );

        etAnswer.setText("");

        if (currentQuestion ==
                questions.length - 1) {

            btnSubmitAnswer.setText(
                    "FINISH INTERVIEW"
            );

        } else {

            btnSubmitAnswer.setText(
                    "SUBMIT ANSWER"
            );
        }
    }

    private void submitAnswer() {

        hideKeyboard();

        String answer = getAnswer();

        if (answer.isEmpty()) {

            etAnswer.setError(
                    "Please write your answer before submitting"
            );

            etAnswer.requestFocus();

            return;
        }

        int questionScore =
                evaluateAnswer(
                        answer,
                        modelKeywords[currentQuestion]
                );

        score += questionScore;

        if (currentQuestion <
                questions.length - 1) {

            currentQuestion++;

            showQuestion();

        } else {

            showFinalResult();
        }
    }

    private void hideKeyboard() {

        InputMethodManager imm =
                (InputMethodManager)
                        getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );

        if (imm != null) {

            imm.hideSoftInputFromWindow(
                    etAnswer.getWindowToken(),
                    0
            );
        }

        etAnswer.clearFocus();
    }

    private String getAnswer() {

        if (etAnswer.getText() == null) {
            return "";
        }

        return etAnswer.getText()
                .toString()
                .trim()
                .toLowerCase();
    }

    private int evaluateAnswer(
            String answer,
            String keywordString
    ) {

        String[] keywords =
                keywordString.split(",");

        int matchedKeywords = 0;

        for (String keyword : keywords) {

            if (answer.contains(
                    keyword.trim()
            )) {

                matchedKeywords++;
            }
        }

        int lengthScore;

        if (answer.length() >= 150) {

            lengthScore = 5;

        } else if (answer.length() >= 80) {

            lengthScore = 3;

        } else {

            lengthScore = 1;
        }

        int keywordScore =
                matchedKeywords * 2;

        int total =
                lengthScore + keywordScore;

        if (total > 10) {
            total = 10;
        }

        return total;
    }

    private void showFinalResult() {

        hideKeyboard();

        int percentage =
                (score * 100) /
                        (questions.length * 10);

        String performance;

        if (percentage >= 80) {

            performance =
                    "Excellent interview performance! 🚀\n\n" +
                            "Your answers show good preparation and clarity.";

        } else if (percentage >= 60) {

            performance =
                    "Good performance!\n\n" +
                            "Improve answer structure and give more specific examples.";

        } else {

            performance =
                    "Keep practicing.\n\n" +
                            "Try giving longer, clearer and more specific answers.";
        }

        tvInterviewStatus.setText(
                "INTERVIEW COMPLETE"
        );

        new AlertDialog.Builder(this)
                .setTitle("Interview Feedback")
                .setMessage(
                        "Overall Score: " +
                                percentage +
                                " / 100\n\n" +

                                "Questions Completed: " +
                                questions.length +
                                " / " +
                                questions.length +
                                "\n\n" +

                                performance
                )
                .setPositiveButton(
                        "BACK TO PLACEMENT",
                        (dialog, which) -> finish()
                )
                .setNegativeButton(
                        "RETRY INTERVIEW",
                        (dialog, which) -> {

                            currentQuestion = 0;
                            score = 0;

                            cardSetup.setVisibility(
                                    View.VISIBLE
                            );

                            layoutInterview.setVisibility(
                                    View.GONE
                            );
                        }
                )
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {

        hideKeyboard();

        super.onDestroy();
    }
}