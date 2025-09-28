package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    
    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private CheckBox cbTerms, cbNewsletter;
    private Button btnSignUp;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        
        initializeViews();
        
        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        // Find views by their IDs
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        cbTerms = findViewById(R.id.cbTerms);
        cbNewsletter = findViewById(R.id.cbNewsletter);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupClickListeners() {
        // Sign up button click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        // Login text click listener (back to login)
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void handleSignUp() {
        // Get text from input fields
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation (Extra Credit requirement)
        if (!validateInputs(fullName, email, password, confirmPassword)) {
            return; // Stop if validation fails
        }

        // Get selected gender
        String gender = getSelectedGender();
        
        // Check if terms are accepted
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show success toast (Extra Credit requirement)
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
        
        // Navigate to User Details (Sign-Up → User Details)
        navigateToUserDetails(fullName, email, gender);
    }

    private boolean validateInputs(String fullName, String email, String password, String confirmPassword) {
        // Check if fields are empty
        if (fullName.isEmpty()) {
            etFullName.setError("Full name cannot be empty");
            etFullName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            etConfirmPassword.requestFocus();
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        // Check password length
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        // Simple email validation
        if (!email.contains("@") || !email.contains(".")) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return false;
        }

        // Check if gender is selected
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }

    private String getSelectedGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == rbMale.getId()) {
            return "Male";
        } else if (selectedId == rbFemale.getId()) {
            return "Female";
        }
        return "Not specified";
    }

    private void navigateToLogin() {
        // Go back to login activity
        finish(); 
    }

    private void navigateToUserDetails(String fullName, String email, String gender) {
        // Create intent to navigate to UserDetailsActivity
        Intent intent = new Intent(SignUpActivity.this, UserDetailsActivity.class);
        
        // Pass user data to UserDetails activity
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("source", "signup");
        intent.putExtra("newsletter", cbNewsletter.isChecked());
        
        startActivity(intent);
        
        
        finish();
    }
}