package com.example.smartcareerassistant;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class DSAPracticeActivity extends AppCompatActivity {

    private TextView tvQuestionNumber;
    private TextView tvTopic;
    private TextView tvDifficulty;
    private TextView tvScore;
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
    private ProgressBar progressDSA;

    private int currentQuestion = 0;
    private int score = 0;
    private int selectedAnswer = -1;

    private final String[] topics = {
            "ARRAYS",
            "STACK",
            "QUEUE",
            "LINKED LIST",
            "SORTING"
    };

    private final String[] difficulties = {
            "EASY",
            "EASY",
            "MEDIUM",
            "MEDIUM",
            "MEDIUM"
    };

    private final String[] questions = {

            "What is the time complexity of searching for an element in an unsorted array?",

            "Which data structure follows the LIFO principle?",

            "Which data structure follows the FIFO principle?",

            "What is the time complexity of inserting a node at the beginning of a linked list?",

            "Which sorting algorithm repeatedly selects the smallest element and places it at the beginning?"
    };

    private final String[][] options = {

            {"O(1)", "O(n)", "O(log n)", "O(n²)"},

            {"Queue", "Stack", "Array", "Tree"},

            {"Stack", "Tree", "Queue", "Graph"},

            {"O(n)", "O(log n)", "O(1)", "O(n²)"},

            {"Bubble Sort", "Selection Sort", "Merge Sort", "Quick Sort"}
    };

    private final int[] correctAnswers = {
            1,
            1,
            2,
            2,
            1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsa_practice);

        initializeViews();
        setupOptionListeners();
        showQuestion();

        btnNext.setOnClickListener(v -> checkAnswer());
    }

    private void initializeViews() {

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvTopic = findViewById(R.id.tvTopic);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvScore = findViewById(R.id.tvScore);
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
        progressDSA = findViewById(R.id.progressDSA);
    }

    private void setupOptionListeners() {

        cardOption1.setOnClickListener(v -> selectOption(0));
        cardOption2.setOnClickListener(v -> selectOption(1));
        cardOption3.setOnClickListener(v -> selectOption(2));
        cardOption4.setOnClickListener(v -> selectOption(3));
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

        tvQuestionNumber.setText(
                String.format(
                        "%02d / %02d",
                        currentQuestion + 1,
                        questions.length
                )
        );

        tvTopic.setText(topics[currentQuestion]);
        tvDifficulty.setText(difficulties[currentQuestion]);

        tvQuestion.setText(questions[currentQuestion]);

        tvOption1.setText(options[currentQuestion][0]);
        tvOption2.setText(options[currentQuestion][1]);
        tvOption3.setText(options[currentQuestion][2]);
        tvOption4.setText(options[currentQuestion][3]);

        progressDSA.setProgress(currentQuestion + 1);

        tvScore.setText(score + " pts");

        if (currentQuestion == questions.length - 1) {
            btnNext.setText("FINISH CHALLENGE");
        } else {
            btnNext.setText("SUBMIT & CONTINUE");
        }
    }

    private void checkAnswer() {

        if (selectedAnswer == -1) {

            Toast.makeText(
                    this,
                    "Select an answer first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (selectedAnswer == correctAnswers[currentQuestion]) {
            score += 20;
        }

        if (currentQuestion < questions.length - 1) {

            currentQuestion++;
            showQuestion();

        } else {

            showResult();
        }
    }

    private void showResult() {

        String performance;

        if (score >= 80) {
            performance = "Excellent problem-solving skills! 🚀";
        } else if (score >= 60) {
            performance = "Good attempt! Keep practicing DSA.";
        } else {
            performance = "Keep practicing. Your logic will improve.";
        }

        new AlertDialog.Builder(this)
                .setTitle("DSA Challenge Complete 🧩")
                .setMessage(
                        "Your Score: " + score + " / 100\n\n" +
                                "Questions Solved: " +
                                (score / 20) + " / 5\n\n" +
                                performance
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
                        }
                )
                .setCancelable(false)
                .show();
    }
}