package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    // Declare UI components
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword, tvStudentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        // Find views by their IDs
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvStudentList = findViewById(R.id.tvStudentList);
    }

    private void setupClickListeners() {
        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Sign up text click listener (Login → Sign-Up navigation)
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });

        // Forgot password click listener
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Student List click listener
        tvStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToStudentList();
            }
        });
    }

    private void handleLogin() {
        // Get text from input fields
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation
        if (username.isEmpty()) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }

        // Simple authentication
        if (isValidCredentials(username, password)) {
            // Show success toast
            Toast.makeText(this, "Login successful! Welcome " + username, Toast.LENGTH_SHORT).show();

            // Navigate to Dashboard (UPDATED - now goes to Dashboard instead of UserDetails)
            navigateToDashboard(username);
        } else {
            // Show error toast
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Simple demo validation
        return !username.isEmpty() && !password.isEmpty() && password.length() >= 3;
    }

    private void navigateToSignUp() {
        // Create intent to navigate to SignUpActivity
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

        // Optional: add animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    // NEW: Navigate to Dashboard after successful login
    private void navigateToDashboard(String username) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish(); // Close login activity
    }

    // Navigate to Student List
    private void navigateToStudentList() {
        Toast.makeText(this, "Opening Student List...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, StudentListActivity.class);
        startActivity(intent);
    }
}