package com.example.smartcareerassistant;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeBuilderActivity extends AppCompatActivity {

    // Personal Information
    private TextInputEditText etResumeName;
    private TextInputEditText etResumeEmail;
    private TextInputEditText etResumePhone;
    private TextInputEditText etLocation;
    private TextInputEditText etLinkedIn;
    private TextInputEditText etGithub;

    // Resume Sections
    private TextInputEditText etObjective;
    private TextInputEditText etEducation;
    private TextInputEditText etSkills;
    private TextInputEditText etLanguages;
    private TextInputEditText etExperience;
    private TextInputEditText etProjects;
    private TextInputEditText etAchievements;

    private MaterialButton btnGenerateResume;

    // A4 size in PDF points
    private static final int PAGE_WIDTH = 595;
    private static final int PAGE_HEIGHT = 842;

    // Left sidebar
    private static final float SIDEBAR_WIDTH = 190f;

    // Colors
    private static final int PURPLE = Color.rgb(103, 80, 164);
    private static final int LIGHT_PURPLE = Color.rgb(238, 229, 244);
    private static final int DARK_TEXT = Color.rgb(45, 45, 45);
    private static final int GRAY_TEXT = Color.rgb(92, 92, 92);
    private static final int LIGHT_GRAY = Color.rgb(220, 220, 220);
    private static final int WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resume_builder);

        // Personal Information
        etResumeName = findViewById(R.id.etResumeName);
        etResumeEmail = findViewById(R.id.etResumeEmail);
        etResumePhone = findViewById(R.id.etResumePhone);
        etLocation = findViewById(R.id.etLocation);
        etLinkedIn = findViewById(R.id.etLinkedIn);
        etGithub = findViewById(R.id.etGithub);

        // Resume Sections
        etObjective = findViewById(R.id.etObjective);
        etEducation = findViewById(R.id.etEducation);
        etSkills = findViewById(R.id.etSkills);
        etLanguages = findViewById(R.id.etLanguages);
        etExperience = findViewById(R.id.etExperience);
        etProjects = findViewById(R.id.etProjects);
        etAchievements = findViewById(R.id.etAchievements);

        btnGenerateResume = findViewById(R.id.btnGenerateResume);

        btnGenerateResume.setOnClickListener(v -> generateResume());
    }

    // =========================================================
    // GENERATE RESUME
    // =========================================================

    private void generateResume() {

        String name = getText(etResumeName);
        String email = getText(etResumeEmail);
        String phone = getText(etResumePhone);
        String education = getText(etEducation);
        String skills = getText(etSkills);
        String experience = getText(etExperience);

        if (TextUtils.isEmpty(name)) {
            etResumeName.setError("Enter your full name");
            etResumeName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etResumeEmail.setError("Enter your email");
            etResumeEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etResumePhone.setError("Enter your phone number");
            etResumePhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(education)) {
            etEducation.setError("Enter your education");
            etEducation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(skills)) {
            etSkills.setError("Enter your skills");
            etSkills.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(experience)) {
            etExperience.setError("Enter your experience or internships");
            etExperience.requestFocus();
            return;
        }

        showResumePreview();
    }

    // =========================================================
    // PREVIEW DIALOG
    // =========================================================

    private void showResumePreview() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Professional Resume")
                .setMessage(
                        "Your resume is ready to generate.\n\n"
                                + "A4 professional two-column layout will be created."
                )
                .setPositiveButton(
                        "CREATE PDF",
                        (dialogInterface, which) -> createResumePdf()
                )
                .setNegativeButton("EDIT", null)
                .create();

        dialog.show();
    }

    // =========================================================
    // CREATE PDF
    // =========================================================

    private void createResumePdf() {

        PdfDocument pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        PAGE_WIDTH,
                        PAGE_HEIGHT,
                        1
                ).create();

        PdfDocument.Page page =
                pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        drawResume(canvas);

        pdfDocument.finishPage(page);

        File documentsFolder = new File(
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "SmartCareerAssistant"
        );

        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs();
        }

        String safeName = getText(etResumeName)
                .replaceAll("[^a-zA-Z0-9]", "_");

        if (safeName.isEmpty()) {
            safeName = "Resume";
        }

        File pdfFile = new File(
                documentsFolder,
                "SmartCareer_" + safeName + ".pdf"
        );

        try {

            FileOutputStream outputStream =
                    new FileOutputStream(pdfFile);

            pdfDocument.writeTo(outputStream);

            outputStream.close();
            pdfDocument.close();

            Toast.makeText(
                    this,
                    "Professional Resume PDF created!",
                    Toast.LENGTH_SHORT
            ).show();

            sharePdf(pdfFile);

        } catch (IOException e) {

            try {
                pdfDocument.close();
            } catch (Exception ignored) {
            }

            Toast.makeText(
                    this,
                    "Unable to create PDF.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // =========================================================
    // DRAW COMPLETE RESUME
    // =========================================================

    private void drawResume(Canvas canvas) {

        // -----------------------------------------------------
        // WHITE PAGE
        // -----------------------------------------------------

        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(WHITE);

        canvas.drawRect(
                0,
                0,
                PAGE_WIDTH,
                PAGE_HEIGHT,
                backgroundPaint
        );

        // -----------------------------------------------------
        // LEFT SIDEBAR
        // -----------------------------------------------------

        Paint sidebarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sidebarPaint.setColor(LIGHT_PURPLE);

        canvas.drawRect(
                0,
                0,
                SIDEBAR_WIDTH,
                PAGE_HEIGHT,
                sidebarPaint
        );

        // Purple header
        Paint headerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerPaint.setColor(PURPLE);

        canvas.drawRect(
                0,
                0,
                SIDEBAR_WIDTH,
                245,
                headerPaint
        );

        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------

        Paint namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        namePaint.setColor(WHITE);
        namePaint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );
        namePaint.setTextSize(22);

        String name =
                getText(etResumeName).toUpperCase();

        float nameY = 95;

        for (String line : wrapText(name, 15)) {

            canvas.drawText(
                    line,
                    22,
                    nameY,
                    namePaint
            );

            nameY += 25;
        }

        // Small white line
        Paint whiteLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteLine.setColor(WHITE);

        canvas.drawRect(
                22,
                nameY + 8,
                72,
                nameY + 10,
                whiteLine
        );

        // -----------------------------------------------------
        // CONTACT
        // -----------------------------------------------------

        float contactY = nameY + 35;

        Paint contactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        contactPaint.setColor(WHITE);
        contactPaint.setTextSize(9);

        contactY = drawContact(
                canvas,
                "Email",
                getText(etResumeEmail),
                contactY,
                contactPaint
        );

        contactY += 7;

        contactY = drawContact(
                canvas,
                "Phone",
                getText(etResumePhone),
                contactY,
                contactPaint
        );

        String location = getText(etLocation);

        if (!location.isEmpty()) {

            contactY += 7;

            contactY = drawContact(
                    canvas,
                    "Location",
                    location,
                    contactY,
                    contactPaint
            );
        }

        String linkedIn = getText(etLinkedIn);

        if (!linkedIn.isEmpty()) {

            contactY += 7;

            contactY = drawContact(
                    canvas,
                    "LinkedIn",
                    linkedIn,
                    contactY,
                    contactPaint
            );
        }

        String github = getText(etGithub);

        if (!github.isEmpty()) {

            contactY += 7;

            drawContact(
                    canvas,
                    "GitHub",
                    github,
                    contactY,
                    contactPaint
            );
        }

        // -----------------------------------------------------
        // SIDEBAR SECTIONS
        // -----------------------------------------------------

        float sidebarY = 275;

        sidebarY = drawEducationSection(
                canvas,
                getText(etEducation),
                sidebarY
        );

        sidebarY += 20;

        sidebarY = drawSkillsSection(
                canvas,
                getText(etSkills),
                sidebarY
        );

        String languages = getText(etLanguages);

        if (!languages.isEmpty()) {

            sidebarY += 18;

            drawLanguagesSection(
                    canvas,
                    languages,
                    sidebarY
            );
        }

        // -----------------------------------------------------
        // RIGHT CONTENT
        // -----------------------------------------------------

        float rightX = SIDEBAR_WIDTH + 25;

        float rightWidth =
                PAGE_WIDTH - rightX - 25;

        float rightY = 48;

        String objective = getText(etObjective);

        if (!objective.isEmpty()) {

            rightY = drawMainSection(
                    canvas,
                    "PROFILE / OBJECTIVE",
                    objective,
                    rightX,
                    rightY,
                    rightWidth
            );

            rightY += 18;
        }

        String experience = getText(etExperience);

        if (!experience.isEmpty()) {

            rightY = drawMainSection(
                    canvas,
                    "EXPERIENCE / INTERNSHIPS",
                    experience,
                    rightX,
                    rightY,
                    rightWidth
            );

            rightY += 18;
        }

        String projects = getText(etProjects);

        if (!projects.isEmpty()) {

            rightY = drawMainSection(
                    canvas,
                    "PROJECTS",
                    projects,
                    rightX,
                    rightY,
                    rightWidth
            );

            rightY += 18;
        }

        String achievements = getText(etAchievements);

        if (!achievements.isEmpty()) {

            drawMainSection(
                    canvas,
                    "ACHIEVEMENTS",
                    achievements,
                    rightX,
                    rightY,
                    rightWidth
            );
        }
    }

    // =========================================================
    // CONTACT SECTION
    // =========================================================

    private float drawContact(
            Canvas canvas,
            String label,
            String value,
            float y,
            Paint valuePaint
    ) {

        Paint labelPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        labelPaint.setColor(WHITE);
        labelPaint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );
        labelPaint.setTextSize(8);

        canvas.drawText(
                label,
                22,
                y,
                labelPaint
        );

        float valueY = y + 11;

        for (String line :
                wrapText(value, 27)) {

            canvas.drawText(
                    line,
                    22,
                    valueY,
                    valuePaint
            );

            valueY += 11;
        }

        return valueY;
    }

    // =========================================================
    // EDUCATION
    // =========================================================

    private float drawEducationSection(
            Canvas canvas,
            String education,
            float y
    ) {

        drawSidebarTitle(
                canvas,
                "EDUCATION",
                y
        );

        Paint bodyPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        bodyPaint.setColor(GRAY_TEXT);
        bodyPaint.setTextSize(8.5f);

        float bodyY = y + 22;

        String[] lines =
                education.split("\\n");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            for (String wrapped :
                    wrapText(line, 25)) {

                canvas.drawText(
                        wrapped,
                        22,
                        bodyY,
                        bodyPaint
                );

                bodyY += 12;
            }

            bodyY += 3;
        }

        return bodyY;
    }

    // =========================================================
    // SKILLS
    // =========================================================

    private float drawSkillsSection(
            Canvas canvas,
            String skills,
            float y
    ) {

        drawSidebarTitle(
                canvas,
                "SKILLS",
                y
        );

        Paint bodyPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        bodyPaint.setColor(GRAY_TEXT);
        bodyPaint.setTextSize(8.5f);

        Paint dotPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        dotPaint.setColor(PURPLE);

        float bodyY = y + 22;

        String[] items =
                skills.split("[,\\n]");

        for (String item : items) {

            item = item.trim();

            if (item.isEmpty()) {
                continue;
            }

            // Skill name
            for (String line :
                    wrapText(item, 25)) {

                canvas.drawText(
                        line,
                        22,
                        bodyY,
                        bodyPaint
                );

                bodyY += 11;
            }

            // Skill rating dots
            float dotX = 22;

            for (int i = 0; i < 5; i++) {

                canvas.drawCircle(
                        dotX,
                        bodyY + 2,
                        2.1f,
                        dotPaint
                );

                dotX += 8;
            }

            bodyY += 15;
        }

        return bodyY;
    }

    // =========================================================
    // LANGUAGES
    // =========================================================

    private float drawLanguagesSection(
            Canvas canvas,
            String languages,
            float y
    ) {

        drawSidebarTitle(
                canvas,
                "LANGUAGES",
                y
        );

        Paint bodyPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        bodyPaint.setColor(GRAY_TEXT);
        bodyPaint.setTextSize(8.5f);

        Paint dotPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        dotPaint.setColor(PURPLE);

        float bodyY = y + 22;

        String[] items =
                languages.split("[,\\n]");

        for (String item : items) {

            item = item.trim();

            if (item.isEmpty()) {
                continue;
            }

            canvas.drawText(
                    item,
                    22,
                    bodyY,
                    bodyPaint
            );

            bodyY += 11;

            // Small language level indicator
            float dotX = 22;

            for (int i = 0; i < 5; i++) {

                canvas.drawCircle(
                        dotX,
                        bodyY + 2,
                        2.1f,
                        dotPaint
                );

                dotX += 8;
            }

            bodyY += 15;
        }

        return bodyY;
    }

    // =========================================================
    // SIDEBAR TITLE
    // =========================================================

    private void drawSidebarTitle(
            Canvas canvas,
            String title,
            float y
    ) {

        Paint titlePaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        titlePaint.setColor(DARK_TEXT);
        titlePaint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );
        titlePaint.setTextSize(10);

        canvas.drawText(
                title,
                22,
                y,
                titlePaint
        );

        Paint linePaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        linePaint.setColor(PURPLE);

        canvas.drawRect(
                22,
                y + 5,
                50,
                y + 7,
                linePaint
        );
    }

    // =========================================================
    // MAIN SECTION
    // =========================================================

    private float drawMainSection(
            Canvas canvas,
            String title,
            String text,
            float x,
            float y,
            float width
    ) {

        Paint titlePaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        titlePaint.setColor(PURPLE);
        titlePaint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );
        titlePaint.setTextSize(11);

        canvas.drawText(
                title,
                x,
                y,
                titlePaint
        );

        // Purple underline
        Paint purpleLine =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        purpleLine.setColor(PURPLE);

        canvas.drawRect(
                x,
                y + 5,
                x + 42,
                y + 7,
                purpleLine
        );

        // Light divider
        Paint dividerPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        dividerPaint.setColor(LIGHT_GRAY);

        canvas.drawRect(
                x + 48,
                y + 6,
                x + width,
                y + 7,
                dividerPaint
        );

        Paint bodyPaint =
                new Paint(Paint.ANTI_ALIAS_FLAG);

        bodyPaint.setColor(DARK_TEXT);
        bodyPaint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.NORMAL
                )
        );
        bodyPaint.setTextSize(9);

        float bodyY = y + 24;

        String[] paragraphs =
                text.split("\\n");

        for (String paragraph : paragraphs) {

            paragraph = paragraph.trim();

            if (paragraph.isEmpty()) {
                bodyY += 6;
                continue;
            }

            boolean bullet =
                    paragraph.startsWith("-")
                            || paragraph.startsWith("•");

            if (bullet) {

                paragraph =
                        paragraph.substring(1).trim();

                String bulletText =
                        "• " + paragraph;

                for (String line :
                        wrapText(bulletText, 51)) {

                    canvas.drawText(
                            line,
                            x,
                            bodyY,
                            bodyPaint
                    );

                    bodyY += 14;
                }

            } else {

                for (String line :
                        wrapText(paragraph, 52)) {

                    canvas.drawText(
                            line,
                            x,
                            bodyY,
                            bodyPaint
                    );

                    bodyY += 14;
                }
            }

            bodyY += 3;
        }

        return bodyY;
    }

    // =========================================================
    // TEXT WRAPPING
    // =========================================================

    private String[] wrapText(
            String text,
            int maxCharacters
    ) {

        if (text == null
                || text.trim().isEmpty()) {

            return new String[]{""};
        }

        List<String> lines =
                new ArrayList<>();

        String[] paragraphs =
                text.split("\\n");

        for (String paragraph : paragraphs) {

            paragraph = paragraph.trim();

            if (paragraph.isEmpty()) {

                lines.add("");

                continue;
            }

            String[] words =
                    paragraph.split("\\s+");

            StringBuilder currentLine =
                    new StringBuilder();

            for (String word : words) {

                if (currentLine.length() == 0) {

                    currentLine.append(word);

                } else if (
                        currentLine.length()
                                + word.length()
                                + 1
                                <= maxCharacters
                ) {

                    currentLine
                            .append(" ")
                            .append(word);

                } else {

                    lines.add(
                            currentLine.toString()
                    );

                    currentLine =
                            new StringBuilder();

                    currentLine.append(word);
                }
            }

            if (currentLine.length() > 0) {

                lines.add(
                        currentLine.toString()
                );
            }
        }

        return lines.toArray(
                new String[0]
        );
    }

    // =========================================================
    // SHARE PDF
    // =========================================================

    private void sharePdf(File pdfFile) {

        Uri pdfUri =
                FileProvider.getUriForFile(
                        this,
                        getPackageName()
                                + ".fileprovider",
                        pdfFile
                );

        Intent shareIntent =
                new Intent(Intent.ACTION_SEND);

        shareIntent.setType(
                "application/pdf"
        );

        shareIntent.putExtra(
                Intent.EXTRA_STREAM,
                pdfUri
        );

        shareIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        startActivity(
                Intent.createChooser(
                        shareIntent,
                        "Share Resume PDF"
                )
        );
    }

    // =========================================================
    // GET TEXT
    // =========================================================

    private String getText(
            TextInputEditText editText
    ) {

        if (editText == null
                || editText.getText() == null) {

            return "";
        }

        return editText.getText()
                .toString()
                .trim();
    }
}