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
    private TextView tvSignUp, tvForgotPassword;

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
        // Find views by their IDs (you'll set these IDs in the XML layout)
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
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

        // Forgot password click listener (optional)
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        // Get text from input fields
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation (Extra Credit requirement)
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

        // Simple authentication (for demo purposes)
        // In real app, you'd connect to a server/database
        if (isValidCredentials(username, password)) {
            // Show success toast (Extra Credit requirement)
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Navigate to User Details (Login successful → User Details)
            navigateToUserDetails(username);
        } else {
            // Show error toast
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Simple demo validation - accept any non-empty credentials
        // In real app, you'd check against database
        return !username.isEmpty() && !password.isEmpty() && password.length() >= 3;
    }

    private void navigateToSignUp() {
        // Create intent to navigate to SignUpActivity
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

        // Optional: add animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void navigateToUserDetails(String username) {
        // Create intent to navigate to UserDetailsActivity
        Intent intent = new Intent(LoginActivity.this, UserDetailsActivity.class);

        // Pass username to UserDetails activity
        intent.putExtra("username", username);
        intent.putExtra("source", "login");

        startActivity(intent);

        // Optional: finish this activity so user can't go back with back button
        finish();
    }
}