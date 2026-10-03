package com.example.smartcareerassistant;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;

public class TechnicalSkillsActivity extends AppCompatActivity {

    private TextView tvProgress;
    private TextView tvTimer;
    private TextView tvScore;
    private TextView tvCategory;
    private TextView tvQuestion;

    private TextView tvOption1;
    private TextView tvOption2;
    private TextView tvOption3;
    private TextView tvOption4;

    private MaterialCardView cardOption1;
    private MaterialCardView cardOption2;
    private MaterialCardView cardOption3;
    private MaterialCardView cardOption4;

    private MaterialButton btnNext;
    private ProgressBar progressTechnical;

    private int currentQuestion = 0;
    private int score = 0;
    private int selectedAnswer = -1;

    private CountDownTimer timer;

    private final String[] categories = {
            "JAVA • OOP",
            "JAVA • BASICS",
            "DBMS • SQL",
            "ANDROID",
            "JAVA • COLLECTIONS",
            "DATABASE",
            "ANDROID • UI",
            "JAVA • EXCEPTION",
            "DSA • BASICS",
            "ANDROID • FIREBASE"
    };

    private final String[] questions = {
            "Which OOP principle allows the same interface to be used for different implementations?",

            "Which keyword is used to inherit a class in Java?",

            "Which SQL command is used to retrieve data from a database?",

            "Which component is commonly used to move from one Activity to another in Android?",

            "Which Java collection stores elements as key-value pairs?",

            "Which database is commonly used in Android for cloud-based NoSQL data storage?",

            "Which Android layout allows views to be positioned using constraints?",

            "Which block is used to handle exceptions in Java?",

            "Which data structure follows the LIFO principle?",

            "Which Firebase service is used for user authentication?"
    };

    private final String[][] options = {

            {"Polymorphism", "Encapsulation", "Inheritance", "Abstraction"},

            {"extends", "implements", "inherits", "super"},

            {"SELECT", "INSERT", "UPDATE", "DELETE"},

            {"Intent", "Fragment", "Service", "Adapter"},

            {"ArrayList", "HashMap", "Stack", "Queue"},

            {"Firebase Firestore", "SQLite", "MySQL", "Oracle"},

            {"LinearLayout", "FrameLayout", "ConstraintLayout", "TableLayout"},

            {"try-catch", "if-else", "switch-case", "for-loop"},

            {"Queue", "Stack", "Array", "Tree"},

            {"Firebase Authentication", "Firebase Storage", "Firebase Analytics", "Firebase Hosting"}
    };

    private final int[] correctAnswers = {
            0, 0, 0, 0, 1, 0, 2, 0, 1, 0
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_skills);

        initializeViews();
        setupOptionListeners();

        btnNext.setOnClickListener(v -> checkAnswer());

        showReadyDialog();
    }

    private void initializeViews() {

        tvProgress = findViewById(R.id.tvProgress);
        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);
        tvCategory = findViewById(R.id.tvCategory);
        tvQuestion = findViewById(R.id.tvQuestion);

        tvOption1 = findViewById(R.id.tvOption1);
        tvOption2 = findViewById(R.id.tvOption2);
        tvOption3 = findViewById(R.id.tvOption3);
        tvOption4 = findViewById(R.id.tvOption4);

        cardOption1 = findViewById(R.id.cardOption1);
        cardOption2 = findViewById(R.id.cardOption2);
        cardOption3 = findViewById(R.id.cardOption3);
        cardOption4 = findViewById(R.id.cardOption4);

        btnNext = findViewById(R.id.btnNext);
        progressTechnical = findViewById(R.id.progressTechnical);
    }

    private void setupOptionListeners() {

        cardOption1.setOnClickListener(v -> selectOption(0));
        cardOption2.setOnClickListener(v -> selectOption(1));
        cardOption3.setOnClickListener(v -> selectOption(2));
        cardOption4.setOnClickListener(v -> selectOption(3));
    }

    private void showReadyDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Ready for Assessment? 🧠")
                .setMessage(
                        "You are about to start a 10-question technical assessment.\n\n" +
                                "⏱ Time limit: 5 minutes\n\n" +
                                "• Read each question carefully.\n" +
                                "• Select one answer for each question.\n" +
                                "• Once started, the timer cannot be paused.\n\n" +
                                "Be ready before starting."
                )
                .setPositiveButton(
                        "START ASSESSMENT",
                        (dialog, which) -> {

                            currentQuestion = 0;
                            score = 0;

                            showQuestion();
                            startTimer();
                        }
                )
                .setNegativeButton(
                        "CANCEL",
                        (dialog, which) -> finish()
                )
                .setCancelable(false)
                .show();
    }

    private void selectOption(int option) {

        selectedAnswer = option;

        resetOptionBorders();

        if (option == 0) {
            cardOption1.setStrokeWidth(4);
        } else if (option == 1) {
            cardOption2.setStrokeWidth(4);
        } else if (option == 2) {
            cardOption3.setStrokeWidth(4);
        } else {
            cardOption4.setStrokeWidth(4);
        }
    }

    private void resetOptionBorders() {

        cardOption1.setStrokeWidth(1);
        cardOption2.setStrokeWidth(1);
        cardOption3.setStrokeWidth(1);
        cardOption4.setStrokeWidth(1);
    }

    private void showQuestion() {

        selectedAnswer = -1;
        resetOptionBorders();

        tvProgress.setText(
                (currentQuestion + 1) + " / " + questions.length
        );

        tvCategory.setText(categories[currentQuestion]);

        tvQuestion.setText(questions[currentQuestion]);

        tvOption1.setText(options[currentQuestion][0]);
        tvOption2.setText(options[currentQuestion][1]);
        tvOption3.setText(options[currentQuestion][2]);
        tvOption4.setText(options[currentQuestion][3]);

        progressTechnical.setProgress(currentQuestion + 1);

        tvScore.setText(String.valueOf(score));

        if (currentQuestion == questions.length - 1) {
            btnNext.setText("FINISH ASSESSMENT");
        } else {
            btnNext.setText("NEXT QUESTION");
        }
    }

    private void checkAnswer() {

        if (selectedAnswer == -1) {
            btnNext.setError("Select an answer");
            return;
        }

        btnNext.setError(null);

        if (selectedAnswer == correctAnswers[currentQuestion]) {
            score++;
        }

        if (currentQuestion < questions.length - 1) {

            currentQuestion++;
            showQuestion();

        } else {

            if (timer != null) {
                timer.cancel();
            }

            showResult();
        }
    }

    private void startTimer() {

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(5 * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                tvTimer.setText(
                        String.format("%02d:%02d", minutes, seconds)
                );
            }

            @Override
            public void onFinish() {

                tvTimer.setText("00:00");

                showResult();
            }

        }.start();
    }

    private void showResult() {

        int finalScore = score * 10;

        String feedback;

        if (finalScore >= 80) {
            feedback = "Excellent technical foundation! 🚀";
        } else if (finalScore >= 60) {
            feedback = "Good work! Keep improving your technical skills.";
        } else {
            feedback = "Keep practicing. You can improve with regular revision.";
        }

        new AlertDialog.Builder(this)
                .setTitle("Assessment Complete 🎯")
                .setMessage(
                        "Your Score: " + finalScore + " / 100\n\n" +
                                "Correct Answers: " + score + " / 10\n\n" +
                                feedback
                )
                .setPositiveButton(
                        "BACK TO PLACEMENT",
                        (dialog, which) -> finish()
                )
                .setNegativeButton(
                        "TRY AGAIN",
                        (dialog, which) -> {

                            currentQuestion = 0;
                            score = 0;

                            showQuestion();
                            startTimer();
                        }
                )
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {

        if (timer != null) {
            timer.cancel();
        }

        super.onDestroy();
    }
}