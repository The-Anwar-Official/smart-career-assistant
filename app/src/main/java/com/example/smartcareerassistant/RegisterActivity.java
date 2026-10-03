package com.example.smartcareerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvBackToLogin.setOnClickListener(v -> {
            finish();
        });

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Enter your name");
                etName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etRegisterEmail.setError("Enter your email");
                etRegisterEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etRegisterPassword.setError("Enter your password");
                etRegisterPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                etRegisterPassword.setError(
                        "Password must be at least 6 characters"
                );
                etRegisterPassword.requestFocus();
                return;
            }

            // Disable button while registration is in progress
            btnRegister.setEnabled(false);

            // Create Firebase Authentication account
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user == null) {

                                btnRegister.setEnabled(true);

                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Account created, but user data could not be loaded.",
                                        Toast.LENGTH_LONG
                                ).show();

                                return;
                            }

                            String uid = user.getUid();

                            // User data for Firestore
                            Map<String, Object> userData = new HashMap<>();

                            userData.put("uid", uid);
                            userData.put("name", name);
                            userData.put("email", email);

                            // Save user profile in Firestore
                            db.collection("users")
                                    .document(uid)
                                    .set(userData)
                                    .addOnSuccessListener(unused -> {

                                        btnRegister.setEnabled(true);

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Account created successfully!",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        Intent intent = new Intent(
                                                RegisterActivity.this,
                                                MainActivity.class
                                        );

                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {

                                        btnRegister.setEnabled(true);

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Account created, but profile could not be saved.",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    });

                        } else {

                            btnRegister.setEnabled(true);

                            // Show the actual Firebase error
                            String errorMessage = "Registration failed.";

                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }

                            Toast.makeText(
                                    RegisterActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });
    }
}