package com.example.assignment2;

import android.content.Intent;
import android.net.Uri;
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
    private TextView tvStudentId, tvFullName, tvEmail, tvPhone, tvGender, tvNewsletter;
    private Button btnLogout, btnEditProfile;
    private TextView tvAucaLink;

    // Store student data
    private String studentPhone;
    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        initializeViews();

        // Display user information
        displayUserInfo();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        // Find views by their IDs
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvSource = findViewById(R.id.tvSource);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvGender = findViewById(R.id.tvGender);
        tvNewsletter = findViewById(R.id.tvNewsletter);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvAucaLink = findViewById(R.id.tvAucaLink);
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
            tvStudentId.setVisibility(View.GONE);
            tvFullName.setVisibility(View.GONE);
            tvEmail.setText("📧 Email: " + username + "@example.com");
            tvPhone.setVisibility(View.GONE);
            tvGender.setVisibility(View.GONE);
            tvNewsletter.setVisibility(View.GONE);

            studentEmail = username + "@example.com";

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
            tvStudentId.setVisibility(View.GONE);
            tvFullName.setText("Full Name: " + fullName);
            tvEmail.setText("📧 Email: " + email);
            tvPhone.setVisibility(View.GONE);
            tvGender.setText("Gender: " + gender);
            tvNewsletter.setText("Newsletter: " + (newsletter ? "Subscribed" : "Not subscribed"));

            // Make all fields visible
            tvFullName.setVisibility(View.VISIBLE);
            tvGender.setVisibility(View.VISIBLE);
            tvNewsletter.setVisibility(View.VISIBLE);

            studentEmail = email;

        } else if ("studentlist".equals(source)) {
            // Data from StudentListActivity (NEW)
            String studentId = intent.getStringExtra("studentId");
            String fullName = intent.getStringExtra("fullName");
            String email = intent.getStringExtra("email");
            String phone = intent.getStringExtra("phone");
            String gender = intent.getStringExtra("gender");

            tvWelcome.setText("Student Details");
            tvUserInfo.setText("View student information");
            tvSource.setText("Source: Student List");

            // Show all student information
            tvStudentId.setText("Student ID: " + studentId);
            tvFullName.setText("Full Name: " + fullName);
            tvEmail.setText("📧 Email: " + email + " (tap to send)");
            tvPhone.setText("📞 Phone: " + phone + " (tap to call)");
            tvGender.setText("Gender: " + gender);
            tvNewsletter.setVisibility(View.GONE);

            // Make all fields visible
            tvStudentId.setVisibility(View.VISIBLE);
            tvFullName.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.VISIBLE);
            tvGender.setVisibility(View.VISIBLE);

            // Store for implicit intents
            studentEmail = email;
            studentPhone = phone;

            // Make email and phone clickable
            makeEmailClickable();
            makePhoneClickable();

        } else {
            // Default case
            tvWelcome.setText("Welcome!");
            tvUserInfo.setText("User Details");
            tvSource.setText("Source: Unknown");
        }
    }

    private void makeEmailClickable() {
        tvEmail.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvEmail.setClickable(true);
        tvEmail.setFocusable(true);
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void makePhoneClickable() {
        tvPhone.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvPhone.setClickable(true);
        tvPhone.setFocusable(true);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void setupClickListeners() {
        // Logout button click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        // Edit Profile button click listener
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditProfile();
            }
        });

        // AUCA website link click listener
        tvAucaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAucaWebsite();
            }
        });
    }

    // Implicit Intent: Send Email
    private void sendEmail() {
        if (studentEmail == null || studentEmail.isEmpty()) {
            Toast.makeText(this, "No email address available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + studentEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello from Instagram Clone");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I wanted to reach out to you...");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            Toast.makeText(this, "Opening email app...", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    // Implicit Intent: Make Phone Call
    private void makePhoneCall() {
        if (studentPhone == null || studentPhone.isEmpty()) {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Option 1: Open dialer with number pre-filled (doesn't require permission)
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + studentPhone));

        try {
            startActivity(dialIntent);
            Toast.makeText(this, "Opening dialer...", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show();
        }

        // Option 2: Direct call (requires CALL_PHONE permission)
        // Intent callIntent = new Intent(Intent.ACTION_CALL);
        // callIntent.setData(Uri.parse("tel:" + studentPhone));
        // startActivity(callIntent);
    }

    private void handleLogout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

    private void handleEditProfile() {
        Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void openAucaWebsite() {
        Toast.makeText(this, "Opening AUCA website...", Toast.LENGTH_SHORT).show();

        String url = "https://www.auca.ac.rw";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "No browser found to open the website", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use logout button to exit", Toast.LENGTH_SHORT).show();
    }
}