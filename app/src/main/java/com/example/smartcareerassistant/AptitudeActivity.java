package com.example.smartcareerassistant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AptitudeActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvQuestion;
    private ProgressBar progressAptitude;
    private RadioGroup radioGroup;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNext;

    private int currentQuestion = 0;
    private int score = 0;

    private final String[] questions = {
            "What is 20% of 150?",
            "If 5 + 7 = 12, what is 12 × 2?",
            "A train travels 60 km in 1 hour. How far will it travel in 3 hours?",
            "What is the next number: 2, 4, 8, 16, ?",
            "If a shirt costs ₹500 and has a 10% discount, what is the final price?"
    };

    private final String[][] options = {
            {"20", "30", "40", "50"},
            {"20", "24", "28", "30"},
            {"120 km", "150 km", "180 km", "200 km"},
            {"20", "24", "32", "36"},
            {"₹440", "₹450", "₹460", "₹480"}
    };

    private final int[] correctAnswers = {
            1, 1, 2, 2, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aptitude);

        initializeViews();

        showQuestion();

        btnNext.setOnClickListener(v -> checkAnswer());
    }

    private void initializeViews() {

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);

        progressAptitude = findViewById(R.id.progressAptitude);

        radioGroup = findViewById(R.id.radioGroup);

        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);

        btnNext = findViewById(R.id.btnNext);
    }

    private void showQuestion() {

        tvQuestionNumber.setText(
                "Question " +
                        (currentQuestion + 1) +
                        " of " +
                        questions.length
        );

        progressAptitude.setProgress(currentQuestion + 1);

        tvQuestion.setText(
                questions[currentQuestion]
        );

        rbOption1.setText(
                options[currentQuestion][0]
        );

        rbOption2.setText(
                options[currentQuestion][1]
        );

        rbOption3.setText(
                options[currentQuestion][2]
        );

        rbOption4.setText(
                options[currentQuestion][3]
        );

        // Clear previous question's selection
        radioGroup.clearCheck();

        if (currentQuestion == questions.length - 1) {

            btnNext.setText("FINISH");

        } else {

            btnNext.setText("NEXT QUESTION");
        }
    }

    private void checkAnswer() {

        int selectedId =
                radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Please select an answer",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedAnswer;

        if (selectedId == R.id.rbOption1) {

            selectedAnswer = 0;

        } else if (selectedId == R.id.rbOption2) {

            selectedAnswer = 1;

        } else if (selectedId == R.id.rbOption3) {

            selectedAnswer = 2;

        } else {

            selectedAnswer = 3;
        }

        if (selectedAnswer ==
                correctAnswers[currentQuestion]) {

            score++;
        }

        if (currentQuestion <
                questions.length - 1) {

            currentQuestion++;

            showQuestion();

        } else {

            showResult();
        }
    }

    private void showResult() {

        int finalScore = score * 20;

        new AlertDialog.Builder(this)
                .setTitle("Aptitude Complete 🎯")
                .setMessage(
                        "Your Score: " +
                                finalScore +
                                " / 100\n\n" +

                                "Correct Answers: " +
                                score +
                                " / 5"
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