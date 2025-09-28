package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserDetailsActivity extends AppCompatActivity {

    // Declare UI components
    private TextView tvWelcome, tvUserInfo, tvSource;
    private TextView tvFullName, tvEmail, tvGender, tvNewsletter;
    private Button btnLogout, btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);
        
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        
        initializeViews();
        
      
        displayUserInfo();
        
        
        setupClickListeners();
    }

    private void initializeViews() {
        // Find views by their IDs
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvSource = findViewById(R.id.tvSource);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvGender = findViewById(R.id.tvGender);
        tvNewsletter = findViewById(R.id.tvNewsletter);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
    }

    private void displayUserInfo() {
        // Get data passed from previous activity
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");

        if ("login".equals(source)) {
            // Data from LoginActivity
            String username = intent.getStringExtra("username");
            
            tvWelcome.setText("Welcome back!");
            tvUserInfo.setText("Logged in as: " + username);
            tvSource.setText("Source: Login");
            
            // Hide fields that are not available from login
            tvFullName.setVisibility(View.GONE);
            tvEmail.setText("Email: " + username + "@example.com"); // Demo data
            tvGender.setVisibility(View.GONE);
            tvNewsletter.setVisibility(View.GONE);
            
        } else if ("signup".equals(source)) {
            // Data from SignUpActivity
            String fullName = intent.getStringExtra("fullName");
            String email = intent.getStringExtra("email");
            String gender = intent.getStringExtra("gender");
            boolean newsletter = intent.getBooleanExtra("newsletter", false);
            
            tvWelcome.setText("Welcome to Instagram!");
            tvUserInfo.setText("Account created successfully");
            tvSource.setText("Source: Sign Up");
            
            // Show all available information
            tvFullName.setText("Full Name: " + fullName);
            tvEmail.setText("Email: " + email);
            tvGender.setText("Gender: " + gender);
            tvNewsletter.setText("Newsletter: " + (newsletter ? "Subscribed" : "Not subscribed"));
            
            // Make all fields visible
            tvFullName.setVisibility(View.VISIBLE);
            tvGender.setVisibility(View.VISIBLE);
            tvNewsletter.setVisibility(View.VISIBLE);
        } else {
            // Default case
            tvWelcome.setText("Welcome!");
            tvUserInfo.setText("User Details");
            tvSource.setText("Source: Unknown");
        }
    }

    private void setupClickListeners() {
        // Logout button click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditProfile();
            }
        });
    }

    private void handleLogout() {
        // Show confirmation toast
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        
        
        
        // Create intent to go back to LoginActivity
        Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
        
       
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        startActivity(intent);
        finish();
    }

    private void handleEditProfile() {
        /
        Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show();
        
        
    }

    @Override
    public void onBackPressed() {
        // Override back button to show logout confirmation
        Toast.makeText(this, "Use logout button to exit", Toast.LENGTH_SHORT).show();
        
    }
}