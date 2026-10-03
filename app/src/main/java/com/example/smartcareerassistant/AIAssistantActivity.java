package com.example.smartcareerassistant;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

public class AIAssistantActivity extends AppCompatActivity {

    private TextInputEditText etQuestion;

    private MaterialButton btnAskAI;
    private MaterialButton btnCareerAdvice;
    private MaterialButton btnSkillsAdvice;
    private MaterialButton btnInterviewAdvice;

    private TextView tvAIResponse;

    private GenerativeModelFutures model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ai_assistant);

        // -------------------------------------------------
        // CONNECT UI
        // -------------------------------------------------

        etQuestion = findViewById(R.id.etQuestion);

        btnAskAI = findViewById(R.id.btnAskAI);
        btnCareerAdvice = findViewById(R.id.btnCareerAdvice);
        btnSkillsAdvice = findViewById(R.id.btnSkillsAdvice);
        btnInterviewAdvice = findViewById(R.id.btnInterviewAdvice);

        tvAIResponse = findViewById(R.id.tvAIResponse);

        // -------------------------------------------------
        // COPY AI RESPONSE
        // -------------------------------------------------

        tvAIResponse.setTextIsSelectable(true);

        tvAIResponse.setOnLongClickListener(v -> {

            String response =
                    tvAIResponse.getText().toString().trim();

            if (response.isEmpty()) {
                return false;
            }

            ClipboardManager clipboard =
                    (ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            ClipData clip =
                    ClipData.newPlainText(
                            "AI Response",
                            response
                    );

            clipboard.setPrimaryClip(clip);

            Toast.makeText(
                    AIAssistantActivity.this,
                    "AI response copied",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        });

        // -------------------------------------------------
        // QUICK QUESTIONS
        // -------------------------------------------------

        btnCareerAdvice.setText(
                "What career should I choose?"
        );

        btnSkillsAdvice.setText(
                "Which skills should I learn?"
        );

        btnInterviewAdvice.setText(
                "How can I improve my interview?"
        );

        int buttonTextColor =
                Color.parseColor("#222222");

        btnCareerAdvice.setTextColor(buttonTextColor);
        btnSkillsAdvice.setTextColor(buttonTextColor);
        btnInterviewAdvice.setTextColor(buttonTextColor);

        // -------------------------------------------------
        // INITIAL AI MESSAGE
        // -------------------------------------------------

        tvAIResponse.setText(
                "Hello! 👋\n\n"
                        + "I am your SmartCareer AI Assistant.\n\n"
                        + "Ask me about careers, skills, Android, Java, "
                        + "resumes, interviews, DSA, aptitude or placements."
        );

        // -------------------------------------------------
        // INITIALIZE GEMINI
        // -------------------------------------------------

        GenerativeModel ai =
                FirebaseAI.getInstance(
                        GenerativeBackend.googleAI()
                ).generativeModel(
                        "gemini-3.7-flash"
                );

        model =
                GenerativeModelFutures.from(ai);

        // -------------------------------------------------
        // ASK AI BUTTON
        // -------------------------------------------------

        btnAskAI.setOnClickListener(v -> {

            hideKeyboard();

            String question =
                    getQuestion();

            if (TextUtils.isEmpty(question)) {

                etQuestion.setError(
                        "Please enter your question"
                );

                etQuestion.requestFocus();

                return;
            }

            askGemini(question);
        });

        // -------------------------------------------------
        // KEYBOARD DONE BUTTON
        // -------------------------------------------------

        etQuestion.setOnEditorActionListener(
                (v, actionId, event) -> {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {

                        hideKeyboard();

                        String question =
                                getQuestion();

                        if (TextUtils.isEmpty(question)) {

                            etQuestion.setError(
                                    "Please enter your question"
                            );

                            return true;
                        }

                        askGemini(question);

                        return true;
                    }

                    return false;
                }
        );

        // -------------------------------------------------
        // QUICK QUESTION 1
        // -------------------------------------------------

        btnCareerAdvice.setOnClickListener(v -> {

            String question =
                    "What career should I choose after BCA? "
                            + "Compare suitable career options for a beginner.";

            etQuestion.setText(
                    "What career should I choose after BCA?"
            );

            hideKeyboard();

            askGemini(question);
        });

        // -------------------------------------------------
        // QUICK QUESTION 2
        // -------------------------------------------------

        btnSkillsAdvice.setOnClickListener(v -> {

            String question =
                    "Which technical skills should a BCA student "
                            + "learn for getting a software job? "
                            + "Give me a practical learning roadmap.";

            etQuestion.setText(
                    "Which skills should I learn?"
            );

            hideKeyboard();

            askGemini(question);
        });

        // -------------------------------------------------
        // QUICK QUESTION 3
        // -------------------------------------------------

        btnInterviewAdvice.setOnClickListener(v -> {

            String question =
                    "How can a fresher improve their technical "
                            + "and HR interview performance? "
                            + "Give practical tips and examples.";

            etQuestion.setText(
                    "How can I improve my interview?"
            );

            hideKeyboard();

            askGemini(question);
        });
    }

    // =====================================================
    // GET QUESTION
    // =====================================================

    private String getQuestion() {

        if (etQuestion.getText() == null) {
            return "";
        }

        return etQuestion.getText()
                .toString()
                .trim();
    }

    // =====================================================
    // ASK GEMINI
    // =====================================================

    private void askGemini(String question) {

        // Show loading message
        tvAIResponse.setText(
                "Gemini AI is thinking...\n\n"
                        + "Please wait."
        );

        // Disable buttons while AI is working
        setButtonsEnabled(false);

        // -------------------------------------------------
        // AI PROMPT
        // -------------------------------------------------

        String prompt =
                "You are SmartCareer Assistant, an AI career "
                        + "and placement assistant inside an Android "
                        + "app for college students and freshers.\n\n"

                        + "The main audience is BCA and other "
                        + "undergraduate students who are preparing "
                        + "for internships, jobs and campus placements.\n\n"

                        + "Answer in simple, clear and beginner-friendly "
                        + "language.\n\n"

                        + "Give practical advice instead of generic "
                        + "motivational statements.\n\n"

                        + "When useful, organize the answer using:\n"
                        + "- Short explanations\n"
                        + "- Bullet points\n"
                        + "- Step-by-step plans\n"
                        + "- Learning roadmaps\n"
                        + "- Examples\n"
                        + "- Interview tips\n\n"

                        + "Focus especially on:\n"
                        + "Career planning\n"
                        + "Software jobs\n"
                        + "Android development\n"
                        + "Java\n"
                        + "Firebase\n"
                        + "Programming\n"
                        + "Data Structures and Algorithms\n"
                        + "Aptitude\n"
                        + "Technical interviews\n"
                        + "HR interviews\n"
                        + "Resume building\n"
                        + "Internships\n"
                        + "Campus placements\n\n"

                        + "If the user asks a broad career question, "
                        + "first understand that they may be a beginner "
                        + "and provide realistic options.\n\n"

                        + "Do not make unrealistic promises about getting "
                        + "a job or salary.\n\n"

                        + "Keep answers focused and useful. "
                        + "Avoid unnecessary repetition.\n\n"

                        + "User's question:\n"
                        + question;

        Content content =
                new Content.Builder()
                        .addText(prompt)
                        .build();

        // -------------------------------------------------
        // GENERATE RESPONSE
        // -------------------------------------------------

        ListenableFuture<GenerateContentResponse> response =
                model.generateContent(content);

        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {

                    @Override
                    public void onSuccess(
                            GenerateContentResponse result
                    ) {

                        runOnUiThread(() -> {

                            setButtonsEnabled(true);

                            String answer =
                                    result.getText();

                            if (answer == null
                                    || answer.trim().isEmpty()) {

                                tvAIResponse.setText(
                                        "Sorry, Gemini did not return "
                                                + "a useful response.\n\n"
                                                + "Please try asking the question "
                                                + "in a different way."
                                );

                            } else {

                                tvAIResponse.setText(
                                        answer.trim()
                                );
                            }
                        });
                    }

                    @Override
                    public void onFailure(
                            Throwable t
                    ) {

                        runOnUiThread(() -> {

                            setButtonsEnabled(true);

                            tvAIResponse.setText(
                                    "Unable to get an AI response right now.\n\n"
                                            + "Please check your internet connection "
                                            + "and try again."
                            );

                            Toast.makeText(
                                    AIAssistantActivity.this,
                                    "AI request failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                        });
                    }

                },
                MoreExecutors.directExecutor()
        );
    }

    // =====================================================
    // ENABLE / DISABLE BUTTONS
    // =====================================================

    private void setButtonsEnabled(
            boolean enabled
    ) {

        btnAskAI.setEnabled(enabled);
        btnCareerAdvice.setEnabled(enabled);
        btnSkillsAdvice.setEnabled(enabled);
        btnInterviewAdvice.setEnabled(enabled);
    }

    // =====================================================
    // HIDE KEYBOARD
    // =====================================================

    private void hideKeyboard() {

        InputMethodManager imm =
                (InputMethodManager)
                        getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );

        if (imm != null) {

            imm.hideSoftInputFromWindow(
                    etQuestion.getWindowToken(),
                    0
            );
        }

        etQuestion.clearFocus();
    }
}